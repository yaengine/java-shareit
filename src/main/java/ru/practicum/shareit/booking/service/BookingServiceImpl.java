package ru.practicum.shareit.booking.service;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.BookingMapper;
import ru.practicum.shareit.booking.BookingState;
import ru.practicum.shareit.booking.BookingStatus;
import ru.practicum.shareit.booking.dto.BookingCreateDto;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.dao.BookingRepository;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.item.ItemMapper;
import ru.practicum.shareit.item.dao.ItemRepository;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.dao.UserRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

import static ru.practicum.shareit.constant.Constants.BOOKING_NOT_FOUND_ERR;
import static ru.practicum.shareit.constant.Constants.USER_NOT_FOUND_ERR;
import static ru.practicum.shareit.constant.Constants.ITEM_NOT_FOUND_ERR;

@Service
@RequiredArgsConstructor
public class BookingServiceImpl implements BookingService {
    private final BookingRepository bookingRepository;
    private final BookingMapper bookingMapper;
    private final UserRepository userRepository;
    private final ItemRepository itemRepository;
    private final ItemMapper itemMapper;

    @Override
    @Transactional
    public BookingDto createBooking(Long userId, BookingCreateDto bookingCreateDto) {
        Item item = itemRepository.findById(bookingCreateDto.getItemId()).orElseThrow(() ->
                new NotFoundException(String.format(ITEM_NOT_FOUND_ERR, bookingCreateDto.getItemId())));
        if (!item.getAvailable()) {
            throw new ValidationException(String.format("Вещь с id %d не доступна", bookingCreateDto.getItemId()));
        }
        Booking booking = Booking.builder()
                .start(bookingCreateDto.getStart())
                .end(bookingCreateDto.getEnd())
                .item(item)
                .booker(userRepository.findById(userId).orElseThrow(() ->
                        new NotFoundException(String.format(USER_NOT_FOUND_ERR, userId))))
                .status(BookingStatus.WAITING)
                .build();

        return addItemDto(bookingRepository.save(booking));
    }

    @Override
    @Transactional
    public BookingDto updateBookingStatus(Long userId, Long bookingId, Boolean isApproved) {
        Booking booking = getBookingByIdWithCheckAccess(userId, bookingId, "owner");
        booking.setStatus(isApproved ? BookingStatus.APPROVED : BookingStatus.REJECTED);

        return addItemDto(bookingRepository.save(booking));
    }

    @Override
    public BookingDto findBookingById(Long userId, Long bookingId) {
        return addItemDto(getBookingByIdWithCheckAccess(userId, bookingId, "ownerOrBooker"));
    }

    @Override
    public List<BookingDto> findBookingsByBookerId(Long bookerId, BookingState state) {
        return bookingRepository.findByBookerId(bookerId).stream()
                .filter(b -> checkByState(b, state))
                .map(this::addItemDto)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<BookingDto> findBookingsByOwnerId(Long ownerId, BookingState state) {
        userRepository.findById(ownerId).orElseThrow(() ->
                new NotFoundException(String.format(USER_NOT_FOUND_ERR, ownerId)));

        return bookingRepository.findByItemOwnerId(ownerId)
                .stream()
                .filter(b -> checkByState(b, state))
                .map(this::addItemDto)
                .toList();
    }

    private Booking getBookingByIdWithCheckAccess(Long userId, Long bookingId, String byWho) {
        Booking booking = bookingRepository.findById(bookingId).orElseThrow(() ->
                new NotFoundException(String.format(BOOKING_NOT_FOUND_ERR, bookingId)));
        boolean isAccessed = switch (byWho) {
            case "owner" -> Objects.equals(booking.getItem().getOwner().getId(), userId);
            case "booker" -> Objects.equals(booking.getBooker().getId(), userId);
            case ("ownerOrBooker") -> Objects.equals(booking.getItem().getOwner().getId(), userId) ||
                    Objects.equals(booking.getBooker().getId(), userId);
            default -> false;
        };

        if (!isAccessed) {
            throw new ValidationException(String.format("Бронирование с id %d Вам не доступно", bookingId));
        } else {
            return booking;
        }
    }

    private boolean checkByState(Booking booking, BookingState state) {
        LocalDateTime curr = LocalDateTime.now();

        return switch (state) {
            case ALL -> true;
            case CURRENT -> booking.getStart().isAfter(curr) && booking.getEnd().isBefore(curr);
            case PAST -> booking.getEnd().isBefore(curr);
            case FUTURE -> booking.getStart().isAfter(curr);
            case WAITING -> booking.getStatus().equals(BookingStatus.WAITING);
            case REJECTED -> booking.getStatus().equals(BookingStatus.REJECTED);
        };
    }

    //Убираем ItemMapper из BookingMapper для избежания цикличной зависимости. Обогащаем BookingDto.
    //@Lazy не помогла
    private BookingDto addItemDto(Booking booking) {
        BookingDto bookingDto = bookingMapper.toBookingDto(booking);
        bookingDto.setItem(itemMapper.toItemDto(booking.getItem()));
        return bookingDto;
    }

}

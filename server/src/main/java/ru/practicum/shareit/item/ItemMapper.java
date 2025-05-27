package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.booking.BookingMapper;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.dao.ItemRequestRepository;
import ru.practicum.shareit.user.User;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;

@Component
@RequiredArgsConstructor
public class ItemMapper {
    private final BookingMapper bookingMapper;
    private final ItemRequestRepository itemRequestRepository;

    public ItemDto toItemDto(Item item) {
        return new ItemDto(
                item.getId(),
                item.getName(),
                item.getDescription(),
                item.getAvailable(),
                item.getRequest() != null ? item.getRequest().getId() : null,
                getLastBooking(item),
                getNextBooking(item),
                null
        );
    }

    public ItemDto toItemDto(Item item, List<CommentDto> commentsDto) {
        return new ItemDto(
                item.getId(),
                item.getName(),
                item.getDescription(),
                item.getAvailable(),
                item.getRequest() != null ? item.getRequest().getId() : null,
                getLastBooking(item),
                getNextBooking(item),
                commentsDto
        );
    }

    public Item toItem(ItemDto itemDto) {
        Item.ItemBuilder itemBuilder =  Item.builder()
                .id(itemDto.getId())
                .name(itemDto.getName())
                .description(itemDto.getDescription())
                .available(itemDto.getAvailable());

        if (itemDto.getRequestId() != null) {
            itemBuilder.request(itemRequestRepository.findById(itemDto.getRequestId()).orElse(null));
        }

        return itemBuilder.build();

    }

    public CommentDto toCommentDto(Comment comment) {
        return CommentDto.builder()
                .text(comment.getText())
                .authorName(comment.getAuthor().getName())
                .created(comment.getCreated())
                .build();
    }

    public Comment toComment(CommentDto commentDto, Item item, User user) {
        return Comment.builder()
                .text(commentDto.getText())
                .author(user)
                .item(item)
                .build();
    }

    private BookingDto getLastBooking(Item item) {
        List<Booking> bookings = item.getBookings();
        if (bookings == null || bookings.isEmpty()) {
            return null;
        }
        Booking booking = bookings.stream()
                .filter(b -> b.getStart().isAfter(LocalDateTime.now()))
                .max(Comparator.comparing(Booking::getStart)).orElse(null);
        if (booking == null) {
            return null;
        } else {
            return bookingMapper.toBookingDto(booking);
        }
    }

    private BookingDto getNextBooking(Item item) {
        List<Booking> bookings = item.getBookings();
        if (bookings == null || bookings.isEmpty()) {
            return null;
        }
        Booking booking = bookings.stream()
                .filter(b -> b.getStart().isAfter(LocalDateTime.now()))
                .min(Comparator.comparing(Booking::getStart)).orElse(null);
        if (booking == null) {
            return null;
        } else {
            return bookingMapper.toBookingDto(booking);
        }
    }
}

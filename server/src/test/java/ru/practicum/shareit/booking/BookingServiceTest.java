package ru.practicum.shareit.booking;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.practicum.shareit.ShareItServer;
import ru.practicum.shareit.booking.dto.BookingCreateDto;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.service.BookingService;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.service.UserService;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertFalse;

@SpringBootTest(classes = ShareItServer.class)
public class BookingServiceTest {
    @Autowired
    private ItemService itemService;

    @Autowired
    private UserService userService;

    @Autowired
    private BookingService bookingService;

    @Test
    void findBookingById() {
        UserDto userDto = UserDto.builder()
                .name("userName0b")
                .email("userName0b@ya.ru")
                .build();
        UserDto user = userService.createUser(userDto);

        ItemDto itemDto = ItemDto.builder()
                .name("item0bName")
                .description("item0bdescription")
                .available(true)
                .build();
        ItemDto newItem = itemService.createItem(itemDto, user);

        BookingCreateDto bookingCreateDto = BookingCreateDto.builder()
                .itemId(newItem.getId())
                .start(LocalDateTime.now())
                .end(LocalDateTime.now().plusMinutes(1))
                .build();

        BookingDto newBooking = bookingService.createBooking(bookingCreateDto.getItemId(), bookingCreateDto);

        assertEquals(newBooking.getId(), bookingService.findBookingById(user.getId(), newBooking.getId()).getId(),
                "Бронирование не нашлось");
    }

    @Test
    void findBookingByIdWithWrongId() {
        assertThrows(Exception.class,
                () -> bookingService.findBookingById(-1L, -1L),
                "Бронирование не должно найтись");
    }

    @Test
    void createBookingWithWrongItem() {
        BookingCreateDto bookingCreateDto = BookingCreateDto.builder()
                .itemId(-1L)
                .start(LocalDateTime.now())
                .end(LocalDateTime.now().plusMinutes(1))
                .build();

        assertThrows(Exception.class,
                () -> bookingService.createBooking(bookingCreateDto.getItemId(), bookingCreateDto),
                "Бронирование не существующей вещи не должно сработать");
    }

    @Test
    void createBooking() {
        UserDto userDto = UserDto.builder()
                .name("userName2b")
                .email("userName2b@ya.ru")
                .build();
        UserDto user = userService.createUser(userDto);

        ItemDto itemDto = ItemDto.builder()
                .name("item2bName")
                .description("item2bdescription")
                .available(true)
                .build();
        ItemDto newItem = itemService.createItem(itemDto, user);

        BookingCreateDto bookingCreateDto = BookingCreateDto.builder()
                .itemId(newItem.getId())
                .start(LocalDateTime.now())
                .end(LocalDateTime.now().plusMinutes(1))
                .build();

        BookingDto newBooking = bookingService.createBooking(bookingCreateDto.getItemId(), bookingCreateDto);

        assertTrue(newBooking.getId() > 0, "Бронирование не создалась");
    }

    @Test
    void updateBookingStatus() {
        UserDto userDto = UserDto.builder()
                .name("userName3b")
                .email("userName3b@ya.ru")
                .build();
        UserDto user = userService.createUser(userDto);

        ItemDto itemDto = ItemDto.builder()
                .name("item3bName")
                .description("item3bdescription")
                .available(true)
                .build();
        ItemDto newItem = itemService.createItem(itemDto, user);

        BookingCreateDto bookingCreateDto = BookingCreateDto.builder()
                .itemId(newItem.getId())
                .start(LocalDateTime.now())
                .end(LocalDateTime.now().plusMinutes(1))
                .build();
        BookingDto newBooking = bookingService.createBooking(bookingCreateDto.getItemId(), bookingCreateDto);
        bookingService.updateBookingStatus(user.getId(), newBooking.getId(), true);

        assertEquals(BookingStatus.APPROVED, bookingService.findBookingById(user.getId(), newBooking.getId())
                        .getStatus(), "Бронирование должно быть подтверждено");
    }

    @Test
    void findBookingsByBookerId() {
        UserDto userDto = UserDto.builder()
                .name("userName4b")
                .email("userName4b@ya.ru")
                .build();
        UserDto user = userService.createUser(userDto);

        ItemDto itemDto = ItemDto.builder()
                .name("item4bName")
                .description("item4bdescription")
                .available(true)
                .build();
        ItemDto newItem = itemService.createItem(itemDto, user);

        BookingCreateDto bookingCreateDto = BookingCreateDto.builder()
                .itemId(newItem.getId())
                .start(LocalDateTime.now())
                .end(LocalDateTime.now().plusMinutes(1))
                .build();
        BookingDto newBooking = bookingService.createBooking(user.getId(), bookingCreateDto);

        List<BookingDto> bookings = bookingService.findBookingsByBookerId(user.getId(), BookingState.ALL);
        assertFalse(bookings.isEmpty(), "Бронирования не нашлись");
    }

    @Test
    void findBookingsByOwnerId() {
        UserDto userDto = UserDto.builder()
                .name("userName5b")
                .email("userName5b@ya.ru")
                .build();
        UserDto user = userService.createUser(userDto);

        ItemDto itemDto = ItemDto.builder()
                .name("item5bName")
                .description("item5bdescription")
                .available(true)
                .build();
        ItemDto newItem = itemService.createItem(itemDto, user);

        BookingCreateDto bookingCreateDto = BookingCreateDto.builder()
                .itemId(newItem.getId())
                .start(LocalDateTime.now())
                .end(LocalDateTime.now().plusMinutes(1))
                .build();
        BookingDto newBooking = bookingService.createBooking(user.getId(), bookingCreateDto);

        List<BookingDto> bookings = bookingService.findBookingsByBookerId(user.getId(), BookingState.ALL);
        assertFalse(bookings.isEmpty(), "Бронирования не нашлись");
    }

}

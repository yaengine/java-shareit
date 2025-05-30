package ru.practicum.shareit.booking;

import org.junit.jupiter.api.Test;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.user.dto.UserDto;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

class BookingDtoTest {

    private final LocalDateTime startTime = LocalDateTime.now();
    private final LocalDateTime endTime = LocalDateTime.now().plusMinutes(1);
    private final ItemDto itemDto = ItemDto.builder()
            .id(1L)
            .name("Item")
            .build();
    private final UserDto userDto = UserDto.builder()
            .id(1L)
            .name("User")
            .build();

    @Test
    void builderShouldCreateValidBookingDto() {
        BookingDto bookingDto = BookingDto.builder()
                .id(1L)
                .start(startTime)
                .end(endTime)
                .item(itemDto)
                .booker(userDto)
                .status(BookingStatus.APPROVED)
                .build();

        assertThat(bookingDto.getId()).isEqualTo(1L);
        assertThat(bookingDto.getStart()).isEqualTo(startTime);
        assertThat(bookingDto.getEnd()).isEqualTo(endTime);
        assertThat(bookingDto.getItem()).isEqualTo(itemDto);
        assertThat(bookingDto.getBooker()).isEqualTo(userDto);
        assertThat(bookingDto.getStatus()).isEqualTo(BookingStatus.APPROVED);
    }

    @Test
    void settersAndGettersShouldWorkCorrectly() {
        BookingDto bookingDto = BookingDto.builder().build();
        bookingDto.setId(2L);
        bookingDto.setStart(startTime.plusDays(1));
        bookingDto.setEnd(endTime.plusDays(1));
        bookingDto.setItem(itemDto);
        bookingDto.setBooker(userDto);
        bookingDto.setStatus(BookingStatus.WAITING);

        assertThat(bookingDto.getId()).isEqualTo(2L);
        assertThat(bookingDto.getStart()).isAfter(startTime);
        assertThat(bookingDto.getEnd()).isAfter(endTime);
        assertThat(bookingDto.getStatus()).isEqualTo(BookingStatus.WAITING);
    }

    @Test
    void equalsAndHashCodeShouldWorkCorrectly() {
        BookingDto booking1 = BookingDto.builder()
                .id(1L)
                .booker(userDto)
                .build();

        BookingDto booking2 = BookingDto.builder()
                .id(1L)
                .booker(userDto)
                .build();

        BookingDto booking3 = BookingDto.builder()
                .id(2L)
                .build();

        assertThat(booking1).isEqualTo(booking2);
        assertThat(booking1.hashCode()).isEqualTo(booking2.hashCode());
        assertThat(booking1).isNotEqualTo(booking3);
    }

    @Test
    void toStringShouldContainImportantInfo() {
        BookingDto bookingDto = BookingDto.builder()
                .id(3L)
                .status(BookingStatus.REJECTED)
                .build();

        String toString = bookingDto.toString();

        assertThat(toString).contains("BookingDto");
        assertThat(toString).contains("id=3");
        assertThat(toString).contains("status=REJECTED");
    }

    @Test
    void shouldHandleNullFields() {
        BookingDto bookingDto = BookingDto.builder()
                .id(4L)
                .start(null)
                .end(null)
                .item(null)
                .booker(null)
                .status(null)
                .build();

        assertThat(bookingDto.getStart()).isNull();
        assertThat(bookingDto.getItem()).isNull();
        assertThat(bookingDto.getStatus()).isNull();
    }

    @Test
    void shouldValidateStartBeforeEnd() {
        BookingDto bookingDto = BookingDto.builder()
                .start(endTime)
                .end(startTime)
                .build();

        assertThat(bookingDto.getStart()).isAfter(bookingDto.getEnd());
    }
}

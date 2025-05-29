package ru.practicum.shareit.booking;

import org.junit.jupiter.api.Test;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.User;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

class BookingTest {

    private final LocalDateTime now = LocalDateTime.now();
    private final User booker = User.builder().id(1L).name("Booker").build();
    private final Item item = Item.builder().id(1L).name("Item").build();

    @Test
    void builderShouldCreateValidBooking() {
        Booking booking = Booking.builder()
                .id(1L)
                .start(now.plusDays(1))
                .end(now.plusDays(2))
                .item(item)
                .booker(booker)
                .status(BookingStatus.WAITING)
                .build();

        assertThat(booking.getId()).isEqualTo(1L);
        assertThat(booking.getStart()).isAfter(now);
        assertThat(booking.getEnd()).isAfter(booking.getStart());
        assertThat(booking.getItem()).isEqualTo(item);
        assertThat(booking.getBooker()).isEqualTo(booker);
        assertThat(booking.getStatus()).isEqualTo(BookingStatus.WAITING);
    }

    @Test
    void noArgsConstructorShouldCreateEmptyBooking() {
        Booking booking = Booking.builder().build();

        assertThat(booking.getId()).isNull();
        assertThat(booking.getStart()).isNull();
        assertThat(booking.getEnd()).isNull();
        assertThat(booking.getItem()).isNull();
        assertThat(booking.getBooker()).isNull();
        assertThat(booking.getStatus()).isNull();
    }

    @Test
    void allArgsConstructorShouldSetAllFields() {
        Booking booking = new Booking(
                2L,
                now.plusHours(1),
                now.plusHours(2),
                item,
                booker,
                BookingStatus.APPROVED
        );

        assertThat(booking.getId()).isEqualTo(2L);
        assertThat(booking.getStatus()).isEqualTo(BookingStatus.APPROVED);
    }

    @Test
    void settersShouldUpdateFields() {
        Booking booking = Booking.builder().build();
        booking.setId(3L);
        booking.setStart(now);
        booking.setEnd(now.plusDays(1));
        booking.setItem(item);
        booking.setBooker(booker);
        booking.setStatus(BookingStatus.REJECTED);

        assertThat(booking.getId()).isEqualTo(3L);
        assertThat(booking.getStatus()).isEqualTo(BookingStatus.REJECTED);
    }

    @Test
    void equalsAndHashCodeShouldWorkCorrectly() {
        Booking booking1 = Booking.builder()
                .id(1L)
                .booker(booker)
                .build();

        Booking booking2 = Booking.builder()
                .id(1L)
                .booker(booker)
                .build();

        Booking booking3 = Booking.builder()
                .id(2L)
                .build();

        assertThat(booking1).isEqualTo(booking2);
        assertThat(booking1.hashCode()).isEqualTo(booking2.hashCode());
        assertThat(booking1).isNotEqualTo(booking3);
    }

    @Test
    void toStringShouldContainImportantInfo() {
        Booking booking = Booking.builder()
                .id(4L)
                .status(BookingStatus.WAITING)
                .build();

        String toString = booking.toString();

        assertThat(toString).contains("Booking");
        assertThat(toString).contains("id=4");
        assertThat(toString).contains("status=WAITING");
    }
}

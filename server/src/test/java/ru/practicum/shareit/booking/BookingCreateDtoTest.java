package ru.practicum.shareit.booking;

import org.junit.jupiter.api.Test;
import ru.practicum.shareit.booking.dto.BookingCreateDto;

import java.time.LocalDateTime;
import static org.assertj.core.api.Assertions.assertThat;

class BookingCreateDtoTest {

    private final LocalDateTime startTime = LocalDateTime.now();
    private final LocalDateTime endTime = LocalDateTime.now().plusMinutes(1);

    @Test
    void builderShouldCreateValidBookingCreateDto() {
        BookingCreateDto dto = BookingCreateDto.builder()
                .itemId(1L)
                .start(startTime)
                .end(endTime)
                .build();

        assertThat(dto.getItemId()).isEqualTo(1L);
        assertThat(dto.getStart()).isEqualTo(startTime);
        assertThat(dto.getEnd()).isEqualTo(endTime);
        assertThat(dto.getStart()).isBefore(dto.getEnd());
    }

    @Test
    void settersAndGettersShouldWorkCorrectly() {
        BookingCreateDto dto = BookingCreateDto.builder().build();
        dto.setItemId(2L);
        dto.setStart(startTime.plusDays(1));
        dto.setEnd(endTime.plusDays(1));

        assertThat(dto.getItemId()).isEqualTo(2L);
        assertThat(dto.getStart()).isAfter(startTime);
        assertThat(dto.getEnd()).isAfter(endTime);
    }

    @Test
    void equalsAndHashCodeShouldWorkCorrectly() {
        BookingCreateDto dto1 = BookingCreateDto.builder()
                .itemId(1L)
                .start(startTime)
                .end(endTime)
                .build();

        BookingCreateDto dto2 = BookingCreateDto.builder()
                .itemId(1L)
                .start(startTime)
                .end(endTime)
                .build();

        BookingCreateDto dto3 = BookingCreateDto.builder()
                .itemId(2L)
                .build();

        assertThat(dto1).isEqualTo(dto2);
        assertThat(dto1.hashCode()).isEqualTo(dto2.hashCode());
        assertThat(dto1).isNotEqualTo(dto3);
    }

    @Test
    void toStringShouldContainImportantInfo() {
        BookingCreateDto dto = BookingCreateDto.builder()
                .itemId(3L)
                .start(startTime)
                .build();

        String toString = dto.toString();

        assertThat(toString).contains("BookingCreateDto");
        assertThat(toString).contains("itemId=3");
        assertThat(toString).contains("start=" + startTime);
    }

    @Test
    void shouldHandleNullFields() {
        BookingCreateDto dto = BookingCreateDto.builder()
                .itemId(null)
                .start(null)
                .end(null)
                .build();

        assertThat(dto.getItemId()).isNull();
        assertThat(dto.getStart()).isNull();
        assertThat(dto.getEnd()).isNull();
    }

    @Test
    void shouldValidateStartBeforeEnd() {
        BookingCreateDto dto = BookingCreateDto.builder()
                .start(endTime)
                .end(startTime)
                .build();

        assertThat(dto.getStart()).isAfter(dto.getEnd());
    }

    @Test
    void shouldCreateDtoWithOnlyRequiredFields() {
        BookingCreateDto dto = BookingCreateDto.builder()
                .itemId(4L)
                .build();

        assertThat(dto.getItemId()).isEqualTo(4L);
        assertThat(dto.getStart()).isNull();
        assertThat(dto.getEnd()).isNull();
    }
}

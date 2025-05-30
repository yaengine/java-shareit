package ru.practicum.shareit.item;

import org.junit.jupiter.api.Test;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class ItemDtoTest {

    private final BookingDto lastBooking = BookingDto.builder()
            .id(1L)
            .start(LocalDateTime.now().minusDays(1))
            .end(LocalDateTime.now().plusHours(1))
            .build();

    private final BookingDto nextBooking = BookingDto.builder()
            .id(2L)
            .start(LocalDateTime.now().plusDays(1))
            .end(LocalDateTime.now().plusDays(2))
            .build();

    private final CommentDto comment = CommentDto.builder()
            .id(1L)
            .text("Test")
            .authorName("User")
            .build();

    @Test
    void builderShouldCreateValidItemDto() {
        ItemDto itemDto = ItemDto.builder()
                .id(1L)
                .name("Item")
                .description("ItemTest")
                .available(true)
                .requestId(10L)
                .lastBooking(lastBooking)
                .nextBooking(nextBooking)
                .comments(List.of(comment))
                .build();

        assertThat(itemDto.getId()).isEqualTo(1L);
        assertThat(itemDto.getName()).isEqualTo("Item");
        assertThat(itemDto.getDescription()).isEqualTo("ItemTest");
        assertThat(itemDto.getAvailable()).isTrue();
        assertThat(itemDto.getRequestId()).isEqualTo(10L);
        assertThat(itemDto.getLastBooking()).isEqualTo(lastBooking);
        assertThat(itemDto.getNextBooking()).isEqualTo(nextBooking);
        assertThat(itemDto.getComments()).hasSize(1);
    }

    @Test
    void allArgsConstructorShouldSetAllFields() {
        ItemDto itemDto = new ItemDto(
                2L,
                "Item",
                "ItemTest",
                false,
                20L,
                lastBooking,
                nextBooking,
                List.of(comment)
        );

        assertThat(itemDto.getId()).isEqualTo(2L);
        assertThat(itemDto.getAvailable()).isFalse();
        assertThat(itemDto.getRequestId()).isEqualTo(20L);
    }

    @Test
    void settersAndGettersShouldWorkCorrectly() {
        ItemDto itemDto = ItemDto.builder().build();
        itemDto.setId(3L);
        itemDto.setName("Item");
        itemDto.setDescription("ItemTest");
        itemDto.setAvailable(true);
        itemDto.setRequestId(30L);
        itemDto.setLastBooking(lastBooking);
        itemDto.setNextBooking(nextBooking);
        itemDto.setComments(List.of(comment));

        assertThat(itemDto.getId()).isEqualTo(3L);
        assertThat(itemDto.getName()).isEqualTo("Item");
        assertThat(itemDto.getRequestId()).isEqualTo(30L);
        assertThat(itemDto.getComments()).hasSize(1);
    }

    @Test
    void equalsAndHashCodeShouldWorkCorrectly() {
        ItemDto item1 = ItemDto.builder()
                .id(1L)
                .name("Item")
                .build();

        ItemDto item2 = ItemDto.builder()
                .id(1L)
                .name("Item")
                .build();

        ItemDto item3 = ItemDto.builder()
                .id(2L)
                .build();

        assertThat(item1).isEqualTo(item2);
        assertThat(item1.hashCode()).isEqualTo(item2.hashCode());
        assertThat(item1).isNotEqualTo(item3);
    }

    @Test
    void shouldHandleNullCollections() {
        ItemDto itemDto = ItemDto.builder()
                .id(4L)
                .comments(null)
                .build();

        assertThat(itemDto.getComments()).isNull();
    }

    @Test
    void shouldHandleEmptyCollections() {
        ItemDto itemDto = ItemDto.builder()
                .id(5L)
                .comments(List.of())
                .build();

        assertThat(itemDto.getComments()).isEmpty();
    }

    @Test
    void toStringShouldContainImportantInfo() {
        ItemDto itemDto = ItemDto.builder()
                .id(6L)
                .name("Test Item")
                .available(false)
                .build();

        String toString = itemDto.toString();

        assertThat(toString).contains("ItemDto");
        assertThat(toString).contains("id=6");
        assertThat(toString).contains("name=Test Item");
        assertThat(toString).contains("available=false");
    }
}

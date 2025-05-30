package ru.practicum.shareit.request;

import org.junit.jupiter.api.Test;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.request.dto.ItemRequestDto;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class ItemRequestDtoTest {

    private final LocalDateTime testTime = LocalDateTime.now();
    private final ItemDto itemDto = ItemDto.builder()
            .id(1L)
            .name("Item")
            .description("Description")
            .available(true)
            .build();

    @Test
    void builderShouldCreateValidItemRequestDto() {
        ItemRequestDto dto = ItemRequestDto.builder()
                .id(1L)
                .description("Need item")
                .items(List.of(itemDto))
                .created(testTime)
                .build();

        assertThat(dto.getId()).isEqualTo(1L);
        assertThat(dto.getDescription()).isEqualTo("Need item");
        assertThat(dto.getItems()).hasSize(1);
        assertThat(dto.getCreated()).isEqualTo(testTime);
    }

    @Test
    void settersAndGettersShouldWorkCorrectly() {
        ItemRequestDto dto = ItemRequestDto.builder().build();
        dto.setId(2L);
        dto.setDescription("Updated description");
        dto.setItems(List.of(itemDto));
        dto.setCreated(testTime.plusDays(1));

        assertThat(dto.getId()).isEqualTo(2L);
        assertThat(dto.getDescription()).isEqualTo("Updated description");
        assertThat(dto.getItems()).hasSize(1);
        assertThat(dto.getCreated()).isAfter(testTime);
    }

    @Test
    void equalsAndHashCodeShouldWorkCorrectly() {
        ItemRequestDto dto1 = ItemRequestDto.builder()
                .id(1L)
                .description("Request")
                .build();

        ItemRequestDto dto2 = ItemRequestDto.builder()
                .id(1L)
                .description("Request")
                .build();

        ItemRequestDto dto3 = ItemRequestDto.builder()
                .id(2L)
                .build();

        assertThat(dto1).isEqualTo(dto2);
        assertThat(dto1.hashCode()).isEqualTo(dto2.hashCode());
        assertThat(dto1).isNotEqualTo(dto3);
    }

    @Test
    void toStringShouldContainImportantInfo() {
        ItemRequestDto dto = ItemRequestDto.builder()
                .id(3L)
                .description("Test request")
                .build();

        String toString = dto.toString();

        assertThat(toString).contains("ItemRequestDto");
        assertThat(toString).contains("id=3");
        assertThat(toString).contains("description=Test request");
    }

    @Test
    void shouldHandleNullItemsList() {
        ItemRequestDto dto = ItemRequestDto.builder()
                .id(4L)
                .items(null)
                .build();

        assertThat(dto.getItems()).isNull();
    }

    @Test
    void shouldHandleEmptyItemsList() {
        ItemRequestDto dto = ItemRequestDto.builder()
                .id(5L)
                .items(List.of())
                .build();

        assertThat(dto.getItems()).isEmpty();
    }
}

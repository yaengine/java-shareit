package ru.practicum.shareit.request;

import org.junit.jupiter.api.Test;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.model.ItemRequest;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class ItemRequestMapperTest {

    private final ItemRequestMapper mapper = new ItemRequestMapper();
    private final LocalDateTime testTime = LocalDateTime.now();

    @Test
    void toItemRequestDto_shouldMapCorrectlyWithoutItems() {

        ItemRequest request = ItemRequest.builder()
                .id(1L)
                .description("test")
                .created(testTime)
                .build();

        ItemRequestDto dto = mapper.toItemRequestDto(request);

        assertThat(dto.getId()).isEqualTo(request.getId());
        assertThat(dto.getDescription()).isEqualTo(request.getDescription());
        assertThat(dto.getCreated()).isEqualTo(request.getCreated());
        assertThat(dto.getItems()).isNull();
    }

    @Test
    void toItemRequestDto_shouldMapCorrectlyWithItems() {

        ItemRequest request = ItemRequest.builder()
                .id(2L)
                .description("test1")
                .created(testTime)
                .build();

        List<ItemDto> items = List.of(
                ItemDto.builder()
                        .id(1L)
                        .name("i1")
                        .description("test1")
                        .available(true)
                        .requestId(3L)
                        .build(),
                ItemDto.builder()
                        .id(2L)
                        .name("i2")
                        .description("test2")
                        .available(true)
                        .requestId(3L)
                        .build()
        );

        ItemRequestDto dto = mapper.toItemRequestDto(request, items);

        assertThat(dto.getId()).isEqualTo(request.getId());
        assertThat(dto.getDescription()).isEqualTo(request.getDescription());
        assertThat(dto.getCreated()).isEqualTo(request.getCreated());
        assertThat(dto.getItems())
                .hasSize(2)
                .extracting(ItemDto::getName)
                .containsExactly("i1", "i2");
    }

    @Test
    void toItemRequestDto_shouldHandleNullItemsList() {
        ItemRequest request = ItemRequest.builder()
                .id(3L)
                .description("test")
                .created(testTime)
                .build();

        ItemRequestDto dto = mapper.toItemRequestDto(request, null);

        assertThat(dto.getId()).isEqualTo(request.getId());
        assertThat(dto.getDescription()).isEqualTo(request.getDescription());
        assertThat(dto.getCreated()).isEqualTo(request.getCreated());
        assertThat(dto.getItems()).isNull();
    }

    @Test
    void toItemRequestDto_shouldHandleEmptyItemsList() {
        ItemRequest request = ItemRequest.builder()
                .id(4L)
                .description("test")
                .created(testTime)
                .build();

        ItemRequestDto dto = mapper.toItemRequestDto(request, List.of());

        assertThat(dto.getId()).isEqualTo(request.getId());
        assertThat(dto.getDescription()).isEqualTo(request.getDescription());
        assertThat(dto.getCreated()).isEqualTo(request.getCreated());
        assertThat(dto.getItems()).isEmpty();
    }
}
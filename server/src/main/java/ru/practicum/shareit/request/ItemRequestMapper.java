package ru.practicum.shareit.request;

import org.springframework.stereotype.Component;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.model.ItemRequest;

import java.util.List;

@Component
public class ItemRequestMapper {
    public ItemRequestDto toItemRequestDto(ItemRequest itemRequest) {
        return  ItemRequestDto.builder()
                        .id(itemRequest.getId())
                        .description(itemRequest.getDescription())
                        .created(itemRequest.getCreated())
                        .build();
    }

    public ItemRequestDto toItemRequestDto(ItemRequest itemRequest, List<ItemDto> itemDtoList) {
        return  ItemRequestDto.builder()
                .id(itemRequest.getId())
                .description(itemRequest.getDescription())
                .items(itemDtoList)
                .created(itemRequest.getCreated())
                .build();

    }
}

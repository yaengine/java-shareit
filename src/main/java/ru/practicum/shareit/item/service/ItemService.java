package ru.practicum.shareit.item.service;

import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.user.dto.UserDto;

import java.util.Collection;

public interface ItemService {
    ItemDto createItem(ItemDto itemDto, UserDto userDto);

    ItemDto updateItemById(ItemDto itemDto, Long userId, Long itemId);

    ItemDto findItemById(Long itemId);

    Collection<ItemDto> findAllByUserId(Long userId);

    Collection<ItemDto> searchItemsByText(String text);

    CommentDto createComment(Long userId, CommentDto commentDto, Long itemId);
}

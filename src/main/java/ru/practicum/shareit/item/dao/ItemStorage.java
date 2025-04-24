package ru.practicum.shareit.item.dao;

import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;

import java.util.Collection;

public interface ItemStorage {
    Item createItem(Item item);
    Item updateItemById(ItemDto itemDto, Long itemId);
    Item findItemById(Long itemId);
    Collection<Item> findAllByUserId(Long userId);
    Collection<Item> searchItemsByText(String text);
}

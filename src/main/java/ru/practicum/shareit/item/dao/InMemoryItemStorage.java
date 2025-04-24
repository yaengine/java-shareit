package ru.practicum.shareit.item.dao;

import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;

import java.util.Collection;
import java.util.List;

public class InMemoryItemStorage implements ItemStorage{
    @Override
    public Item createItem(Item item) {
        return null;
    }

    @Override
    public Item updateItemById(ItemDto itemDto, Long itemId) {
        return null;
    }

    @Override
    public Item findItemById(Long itemId) {
        return null;
    }

    @Override
    public Collection<Item> findAllByUserId(Long userId) {
        return List.of();
    }

    @Override
    public Collection<Item> searchItemsByText(String text) {
        return List.of();
    }
}

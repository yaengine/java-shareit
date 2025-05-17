package ru.practicum.shareit.item.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.item.ItemMapper;
import ru.practicum.shareit.item.dao.ItemRepository;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.UserMapper;
import ru.practicum.shareit.user.dto.UserDto;

import java.util.ArrayList;
import java.util.Collection;
import java.util.stream.Collectors;

import static ru.practicum.shareit.constant.Constants.ITEM_NOT_FOUND_ERR;

@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {
    private final ItemMapper itemMapper;
    private final UserMapper userMapper;
    private final ItemRepository itemRepository;

    @Override
    public ItemDto createItem(ItemDto itemDto, UserDto userDto) {
        Item item = itemMapper.toItem(itemDto);
        item.setOwner(userMapper.toUser(userDto));

        return itemMapper.toItemDto(itemRepository.save(item));
    }

    @Override
    public ItemDto updateItemById(ItemDto itemDto, Long userId, Long itemId) {
        Item item = isUserOwnerOfItem(itemId, userId);
        Item newItem = itemMapper.toItem(itemDto);
        if (item != null) {
            item.setName(newItem.getName() != null ? newItem.getName() : item.getName());
            item.setDescription(newItem.getDescription() != null ? newItem.getDescription() : item.getDescription());
            item.setAvailable(newItem.getAvailable() != null ? newItem.getAvailable() : item.getAvailable());
            item.setRequest(newItem.getRequest() != null ? newItem.getRequest() : item.getRequest());

            return itemMapper.toItemDto(itemRepository.save(item));
        } else {
            throw new ValidationException(String.format("Вещь с id %d не принадлежит пользователю с id %d",
                    itemId, userId));
        }
    }

    @Override
    public ItemDto findItemById(Long itemId) {
        Item item = itemRepository.findById(itemId).orElseThrow(() ->
                new ValidationException(String.format(ITEM_NOT_FOUND_ERR, itemId)));
        return itemMapper.toItemDto(item);
    }

    @Override
    public Collection<ItemDto> findAllByUserId(Long userId) {
        return itemRepository.findByOwnerId(userId).stream()
                .map(itemMapper::toItemDto)
                .collect(Collectors.toList());
    }

    @Override
    public Collection<ItemDto> searchItemsByText(String text) {
        if (text == null || text.isEmpty()) {
            return new ArrayList<>();
        }
        return itemRepository.searchItemsByText(text).stream()
                .map(itemMapper::toItemDto)
                .collect(Collectors.toList());
    }

    private Item isUserOwnerOfItem(Long itemId, Long userId) {
        Item item = itemRepository.findById(itemId).orElseThrow(() ->
                new ValidationException(String.format(ITEM_NOT_FOUND_ERR, itemId)));

        if (userId.equals(item.getOwner().getId())) {
            return item;
        } else {
            return null;
        }
    }
}

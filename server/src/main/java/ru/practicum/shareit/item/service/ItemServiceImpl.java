package ru.practicum.shareit.item.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.dao.BookingRepository;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.item.ItemMapper;
import ru.practicum.shareit.item.dao.CommentRepository;
import ru.practicum.shareit.item.dao.ItemRepository;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserMapper;
import ru.practicum.shareit.user.dao.UserRepository;
import ru.practicum.shareit.user.dto.UserDto;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import static ru.practicum.shareit.constant.Constants.ITEM_NOT_FOUND_ERR;
import static ru.practicum.shareit.constant.Constants.USER_NOT_FOUND_ERR;

@Slf4j
@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {
    private final ItemMapper itemMapper;
    private final UserMapper userMapper;
    private final ItemRepository itemRepository;
    private final CommentRepository commentRepository;
    private final UserRepository userRepository;
    private final BookingRepository bookingRepository;

    @Override
    @Transactional
    public ItemDto createItem(ItemDto itemDto, UserDto userDto) {
        Item item = itemMapper.toItem(itemDto);
        item.setOwner(userMapper.toUser(userDto));

        return itemMapper.toItemDto(itemRepository.save(item));
    }

    @Override
    @Transactional
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
        List<CommentDto> comments = commentRepository.findByItemId(itemId).stream()
                .map(itemMapper::toCommentDto)
                .toList();
        return itemMapper.toItemDto(item, comments);
    }

    @Override
    public Collection<ItemDto> findAllByUserId(Long userId) {
        return itemRepository.findByOwnerId(userId).stream()
                .map(i -> {
                    List<CommentDto> comments = commentRepository.findByItemId(i.getId()).stream()
                            .map(itemMapper::toCommentDto)
                            .toList();
                    return itemMapper.toItemDto(i, comments);
                })
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

    @Override
    @Transactional
    public CommentDto createComment(Long userId, CommentDto commentDto, Long itemId) {
        User user = userRepository.findById(userId).orElseThrow(() ->
                new NotFoundException(String.format(USER_NOT_FOUND_ERR, userId)));
        Item item = itemRepository.findById(itemId).orElseThrow(() ->
                new ValidationException(String.format(ITEM_NOT_FOUND_ERR, itemId)));

        List<Booking> bookingsByBookerAndItem = bookingRepository.findByBookerIdAndItemId(userId, itemId);
        log.debug("bookingsByBookerAndItem содержит {} записей", bookingsByBookerAndItem.size());
        log.debug("время проверки будет {} ", LocalDateTime.now());

        boolean isBookerTakeItem = bookingsByBookerAndItem.stream()
                .anyMatch(b -> b.getStart().isBefore(LocalDateTime.now()));

        if (isBookerTakeItem) {
            return itemMapper.toCommentDto(commentRepository.save(itemMapper.toComment(commentDto, item, user)));
        } else {
            throw new ValidationException(String.format("Пользователь с id %d не бронировал вещь с id %d", userId,
                    itemId));
        }
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

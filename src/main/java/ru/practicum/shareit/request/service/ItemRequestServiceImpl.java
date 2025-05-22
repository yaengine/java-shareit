package ru.practicum.shareit.request.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.ItemMapper;
import ru.practicum.shareit.item.dao.ItemRepository;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.ItemRequestMapper;
import ru.practicum.shareit.request.dao.ItemRequestRepository;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.dao.UserRepository;
import ru.practicum.shareit.user.dto.UserDto;

import java.util.Comparator;
import java.util.List;

import static ru.practicum.shareit.constant.Constants.ITEM_REQUEST_NOT_FOUND_ERR;
import static ru.practicum.shareit.constant.Constants.USER_NOT_FOUND_ERR;

@Service
@RequiredArgsConstructor
public class ItemRequestServiceImpl implements ItemRequestService {
    private final UserRepository userRepository;
    private final ItemRequestMapper itemRequestMapper;
    private final ItemRequestRepository itemRequestRepository;
    private final ItemRepository itemRepository;
    private final ItemMapper itemMapper;

    @Override
    @Transactional
    public ItemRequestDto createBooking(Long userId, ItemRequestDto itemRequestDto) {
        ItemRequest itemRequest = ItemRequest.builder()
                .description(itemRequestDto.getDescription())
                .requestor(userRepository.findById(userId).orElseThrow(() ->
                        new NotFoundException(String.format(USER_NOT_FOUND_ERR, userId))))
                .build();
        return itemRequestMapper.toItemRequestDto(itemRequestRepository.save(itemRequest));
    }

    @Override
    public List<ItemRequestDto> findUserRequests(Long userId) {
        return itemRequestRepository.findByRequestorId(userId).stream()
                .map(r -> {
                    List<ItemDto> items = itemRepository.findByRequestId(r.getId()).stream()
                            .map(itemMapper::toItemDto)
                            .toList();
                    return itemRequestMapper.toItemRequestDto(r, items);
                })
                .sorted(Comparator.comparing(ItemRequestDto::getCreated).reversed())
                .toList();
    }

    @Override
    public List<ItemRequestDto> findAllRequests() {
        return itemRequestRepository.findAll().stream()
                .map(r -> {
                    List<ItemDto> items = itemRepository.findByRequestId(r.getId()).stream()
                            .map(itemMapper::toItemDto)
                            .toList();
                    return itemRequestMapper.toItemRequestDto(r, items);
                })
                .sorted(Comparator.comparing(ItemRequestDto::getCreated).reversed())
                .toList();
    }

    @Override
    public ItemRequestDto findRequestById(Long requestId) {
        ItemRequest itemRequest = itemRequestRepository.findById(requestId).orElseThrow(() ->
                new NotFoundException(String.format(ITEM_REQUEST_NOT_FOUND_ERR, requestId)));
        List<ItemDto> items = itemRepository.findByRequestId(itemRequest.getId()).stream()
                .map(itemMapper::toItemDto)
                .toList();
        return itemRequestMapper.toItemRequestDto(itemRequest, items);
    }
}

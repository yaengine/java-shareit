package ru.practicum.shareit.request.service;

import ru.practicum.shareit.request.dto.ItemRequestDto;

import java.util.List;

public interface ItemRequestService {
    ItemRequestDto createBooking(Long userId, ItemRequestDto itemRequestDto);

    List<ItemRequestDto> findUserRequests(Long userId);

    List<ItemRequestDto> findAllRequests();

    ItemRequestDto findRequestById (Long requestId);
}

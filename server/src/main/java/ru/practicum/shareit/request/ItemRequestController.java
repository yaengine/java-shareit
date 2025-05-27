package ru.practicum.shareit.request;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.service.ItemRequestService;

import java.util.List;

import static ru.practicum.shareit.constant.Constants.X_SHARER_USER_ID;

@RestController
@RequestMapping(path = "/requests")
@RequiredArgsConstructor
public class ItemRequestController {

    private final ItemRequestService itemRequestService;

    @PostMapping
    public ItemRequestDto createRequest(@RequestHeader(X_SHARER_USER_ID) Long userId,
                                        @RequestBody ItemRequestDto itemRequestDto) {
        return itemRequestService.createBooking(userId, itemRequestDto);
    }

    @GetMapping
    public List<ItemRequestDto> getUserRequests(@RequestHeader(X_SHARER_USER_ID) Long userId) {
        return itemRequestService.findUserRequests(userId);
    }

    @GetMapping("/all")
    public List<ItemRequestDto> getAllRequests() {
        return itemRequestService.findAllRequests();
    }

    @GetMapping("{requestId}")
    public ItemRequestDto getAllRequests(@RequestHeader(X_SHARER_USER_ID) Long userId,
                                         @PathVariable Long requestId) {
        return itemRequestService.findRequestById(requestId);
    }
}

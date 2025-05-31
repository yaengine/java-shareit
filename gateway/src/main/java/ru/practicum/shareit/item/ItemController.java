package ru.practicum.shareit.item;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;

import static ru.practicum.shareit.contant.Constants.X_SHARER_USER_ID;

@Controller
@RequestMapping(path = "/items")
@RequiredArgsConstructor
@Slf4j
@Validated
public class ItemController {
    private final ItemClient itemClient;

    @PostMapping
    public ResponseEntity<Object> createItem(@RequestHeader(X_SHARER_USER_ID) Long userId,
                                             @RequestBody @Valid ItemDto itemDto) {
        return itemClient.createItem(userId, itemDto);
    }

    @PatchMapping("/{itemId}")
    public ResponseEntity<Object> updateItemById(@RequestHeader(X_SHARER_USER_ID) Long userId,
                                                 @RequestBody ItemDto itemDto,
                                                 @PathVariable("itemId") Long itemId) {
        return itemClient.updateItemById(userId, itemDto, itemId);
    }

    @GetMapping("/{itemId}")
    public ResponseEntity<Object> findItemById(@RequestHeader(X_SHARER_USER_ID) Long userId,
                                               @PathVariable("itemId") Long itemId) {
        return itemClient.findItemById(userId, itemId);
    }

    @GetMapping
    public ResponseEntity<Object> findAllByUserId(@RequestHeader(X_SHARER_USER_ID) Long userId) {
        return itemClient.findAllByUserId(userId);
    }

    @GetMapping("/search")
    public ResponseEntity<Object> searchItemsByText(@RequestHeader(X_SHARER_USER_ID) Long userId,
                                                    @RequestParam(name = "text") String text) {
        return itemClient.searchItemsByText(userId, text);
    }

    @PostMapping("/{itemId}/comment")
    public ResponseEntity<Object> createComment(@RequestHeader(X_SHARER_USER_ID) Long userId,
                                                @RequestBody CommentDto commentDto,
                                                @PathVariable Long itemId) {
        return itemClient.createComment(userId, commentDto, itemId);
    }
}

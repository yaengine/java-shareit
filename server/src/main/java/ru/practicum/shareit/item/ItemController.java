package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.service.UserService;

import java.util.Collection;

import static ru.practicum.shareit.constant.Constants.X_SHARER_USER_ID;

@RestController
@RequestMapping("/items")
@RequiredArgsConstructor
public class ItemController {
    private final ItemService itemService;
    private final UserService userService;

    @PostMapping
    public ItemDto createItem(@RequestHeader(X_SHARER_USER_ID) Long userId, @RequestBody ItemDto itemDto) {
        UserDto userDto = userService.findUserById(userId);
        return itemService.createItem(itemDto, userDto);
    }

    @PatchMapping("/{itemId}")
    public ItemDto updateItemById(@RequestHeader(X_SHARER_USER_ID) Long userId, @RequestBody ItemDto itemDto,
                                  @PathVariable("itemId") Long itemId) {
        userService.findUserById(userId);
        return itemService.updateItemById(itemDto, userId, itemId);
    }

    @GetMapping("/{itemId}")
    public ItemDto findItemById(@RequestHeader(X_SHARER_USER_ID) Long userId, @PathVariable("itemId") Long itemId) {
        userService.findUserById(userId);
        return itemService.findItemById(itemId);
    }

    @GetMapping
    public Collection<ItemDto> findAllByUserId(@RequestHeader(X_SHARER_USER_ID) Long userId) {
        userService.findUserById(userId);
        return itemService.findAllByUserId(userId);
    }

    @GetMapping("/search")
    public Collection<ItemDto> searchItemsByText(@RequestHeader(X_SHARER_USER_ID) Long userId,
                                                 @RequestParam(name = "text") String text) {
        userService.findUserById(userId);
        return itemService.searchItemsByText(text);
    }

    @PostMapping("/{itemId}/comment")
    public CommentDto createComment(@RequestHeader(X_SHARER_USER_ID) Long userId,
                                    @RequestBody CommentDto commentDto,
                                    @PathVariable Long itemId) {
        return itemService.createComment(userId, commentDto, itemId);
    }
}

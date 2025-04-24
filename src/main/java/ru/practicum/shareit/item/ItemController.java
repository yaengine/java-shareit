package ru.practicum.shareit.item;

import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.service.UserService;

import java.util.Collection;

/**
 * TODO Sprint add-controllers.
 */
@RestController
@RequestMapping("/items")
public class ItemController {
    ItemService itemService;
    UserService userService;

    @PostMapping
    public ItemDto createItem(@RequestHeader("X-Sharer-User-Id") Long userId, @RequestBody ItemDto itemDto) {
        UserDto userDto = userService.findUserById(userId);
        return itemService.createItem(itemDto, userDto);
    }

    @PatchMapping("/{itemId}")
    public ItemDto updateItemById(@RequestHeader("X-Sharer-User-Id") Long userId, @RequestBody ItemDto itemDto,
                                  @PathVariable("itemId") Long itemId) {
        userService.findUserById(userId);
        return itemService.updateItemById(itemDto, userId, itemId);
    }

    @GetMapping("/{itemId}")
    public ItemDto findItemById(@RequestHeader("X-Sharer-User-Id") Long userId, @PathVariable("itemId") Long itemId) {
        userService.findUserById(userId);
        return itemService.findItemById(itemId);
    }

    @GetMapping
    public Collection<ItemDto> findAllByUserId(@RequestHeader("X-Sharer-User-Id") Long userId) {
        userService.findUserById(userId);
        return itemService.findAllByUserId(userId);
    }

    @GetMapping("/search")
    public Collection<ItemDto> searchItemsByText(@RequestHeader("X-Sharer-User-Id") Long userId,
                                                 @RequestParam(name = "text") String text) {
        userService.findUserById(userId);
        return itemService.searchItemsByText(text);
    }
}

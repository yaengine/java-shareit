package ru.practicum.shareit.item;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.practicum.shareit.ShareItServer;
import ru.practicum.shareit.booking.dto.BookingCreateDto;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.service.BookingService;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.service.UserService;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(classes = ShareItServer.class)
public class ItemServiceTest {
    @Autowired
    private ItemService itemService;

    @Autowired
    private UserService userService;

    @Autowired
    private BookingService bookingService;

    @Test
    void findItemById() {
        UserDto userDto = UserDto.builder()
                .name("userName0i")
                .email("userName0i@ya.ru")
                .build();
        UserDto user = userService.createUser(userDto);

        ItemDto itemDto = ItemDto.builder()
                .name("item0Name")
                .description("item0description")
                .available(true)
                .build();

        ItemDto newItem = itemService.createItem(itemDto, user);

        assertEquals(newItem.getId(), itemService.findItemById(newItem.getId()).getId(), "Вещь не нашелась");
    }

    @Test
    void findItemByIdWithWrongId() {
        assertThrows(Exception.class,
                () -> itemService.findItemById(-1L),
                "Вещь не должна найтись");
    }

    @Test
    void createItemWithoutUser() {
        ItemDto itemDto = ItemDto.builder()
                .name("item1Name")
                .description("item1description")
                .available(true)
                .build();

        assertThrows(Exception.class,
                () -> itemService.createItem(itemDto, null),
                "Создание вещи без пользователя не должно сработать");
    }

    @Test
    void createItem() {
        UserDto userDto = UserDto.builder()
                .name("userName2i")
                .email("userName2i@ya.ru")
                .build();
        UserDto user = userService.createUser(userDto);

        ItemDto itemDto = ItemDto.builder()
                .name("item2Name")
                .description("item2description")
                .available(true)
                .build();

        ItemDto newItem = itemService.createItem(itemDto, user);

        assertTrue(newItem.getId() > 0, "Вещь не создалась");
    }

    @Test
    void updateItem() {
        UserDto userDto = UserDto.builder()
                .name("userName3i")
                .email("userName3i@ya.ru")
                .build();
        UserDto user = userService.createUser(userDto);

        ItemDto itemDto = ItemDto.builder()
                .name("item3iName")
                .description("item3idescription")
                .available(true)
                .build();

        ItemDto newItem = itemService.createItem(itemDto, user);
        newItem.setName("item3NameNew");

        ItemDto updItem = itemService.updateItemById(newItem, user.getId(), newItem.getId());

        assertEquals("item3NameNew", updItem.getName(), "Имя вещи не изменилось");
    }

    @Test
    void updateItemOtherUser() {
        UserDto userDto = UserDto.builder()
                .name("userName3i1")
                .email("userName3i1@ya.ru")
                .build();
        UserDto user = userService.createUser(userDto);

        UserDto userDto1 = UserDto.builder()
                .name("userName3i2")
                .email("userName3i2@ya.ru")
                .build();
        UserDto user1 = userService.createUser(userDto1);

        ItemDto itemDto = ItemDto.builder()
                .name("item3iNam1e")
                .description("item3idescription1")
                .available(true)
                .build();

        ItemDto newItem = itemService.createItem(itemDto, user);
        newItem.setName("item3NameNew1");

        assertThrows(Exception.class,
                () -> itemService.updateItemById(newItem, user1.getId(), newItem.getId()),
                "Обновление вещи другого пользователя не должно сработать");
    }

    @Test
    void findAllByUserId() {
        UserDto userDto = UserDto.builder()
                .name("userName4i")
                .email("userName4i@ya.ru")
                .build();
        UserDto user = userService.createUser(userDto);

        ItemDto itemDto = ItemDto.builder()
                .name("item4Name")
                .description("item4description")
                .available(true)
                .build();

        itemService.createItem(itemDto, user);

        List<ItemDto> items = (List<ItemDto>) itemService.findAllByUserId(user.getId());
        assertFalse(items.isEmpty(), "Вещи не нашлись");
    }

    @Test
    void searchItemsByText() {
        UserDto userDto = UserDto.builder()
                .name("userName5i")
                .email("userName5i@ya.ru")
                .build();
        UserDto user = userService.createUser(userDto);

        ItemDto itemDto = ItemDto.builder()
                .name("item5Name")
                .description("item5description")
                .available(true)
                .build();

        itemService.createItem(itemDto, user);

        List<ItemDto> items = (List<ItemDto>) itemService.searchItemsByText("item5Name");
        assertFalse(items.isEmpty(), "Вещи не нашлись");
    }

    @Test
    void searchItemsByTextWithEmptyText() {
        List<ItemDto> items = (List<ItemDto>) itemService.searchItemsByText(null);
        assertTrue(items.isEmpty(), "Вещи нашлись");
    }

    @Test
    void createComment() {
        UserDto userDto = UserDto.builder()
                .name("userName6i")
                .email("userName6i@ya.ru")
                .build();
        UserDto user = userService.createUser(userDto);

        ItemDto itemDto = ItemDto.builder()
                .name("item6Name")
                .description("item6description")
                .available(true)
                .build();
        ItemDto newItem = itemService.createItem(itemDto, user);

        BookingCreateDto bookingCreateDto = BookingCreateDto.builder()
                .itemId(newItem.getId())
                .start(LocalDateTime.now())
                .end(LocalDateTime.now().plusMinutes(1))
                .build();

        BookingDto newBooking = bookingService.createBooking(user.getId(), bookingCreateDto);

        CommentDto comment = CommentDto.builder()
                .text("commentText")
                .authorName(user.getName())
                .created(LocalDateTime.now())
                .build();

        CommentDto newCommnent = itemService.createComment(user.getId(), comment, newItem.getId());

        assertEquals("commentText", newCommnent.getText(), "Комментарий не создался");
    }

    @Test
    void createCommentWithoutUser() {
        UserDto userDto = UserDto.builder()
                .name("userName6i1")
                .email("userName6i1@ya.ru")
                .build();
        UserDto user = userService.createUser(userDto);

        ItemDto itemDto = ItemDto.builder()
                .name("item61Name")
                .description("item61description")
                .available(true)
                .build();
        ItemDto newItem = itemService.createItem(itemDto, user);

        CommentDto comment = CommentDto.builder()
                .text("commentText")
                .authorName(user.getName())
                .created(LocalDateTime.now())
                .build();

        assertThrows(Exception.class,
                () -> itemService.createComment(0L, comment, newItem.getId()),
                "Создание комментария не существующего пользователя не должно сработать");
    }

    @Test
    void createCommentWitWrongItem() {
        UserDto userDto = UserDto.builder()
                .name("userName6i2")
                .email("userName6i2@ya.ru")
                .build();
        UserDto user = userService.createUser(userDto);


        CommentDto comment = CommentDto.builder()
                .text("commentText")
                .authorName(user.getName())
                .created(LocalDateTime.now())
                .build();

        assertThrows(Exception.class,
                () -> itemService.createComment(user.getId(), comment, 0L),
                "Создание комментария не существующего пользователя не должно сработать");
    }

}

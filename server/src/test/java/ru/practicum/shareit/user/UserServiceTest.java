package ru.practicum.shareit.user;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.practicum.shareit.ShareItServer;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.service.UserService;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(classes = ShareItServer.class)
public class UserServiceTest {
   @Autowired
    private UserService userService;

    @Test
    void createUser() {
        UserDto userDto = UserDto.builder()
                .name("userName")
                .email("userName@ya.ru")
                .build();
        UserDto user = userService.createUser(userDto);
        assertTrue(user.getId() > 0, "Пользователь не создался");
    }

    @Test
    void updateUser() {
        UserDto userDto = UserDto.builder()
                .name("userName")
                .email("userName@ya.ru")
                .build();
        UserDto user = userService.createUser(userDto);
        user.setName("newName");
        UserDto updUser = userService.updateUserById(user, user.getId());
        assertEquals("newName", updUser.getName(), "Имя пользователя не изменилось");
    }

    @Test
    void updateUserWhoNotExist() {
        UserDto userDto = UserDto.builder()
                .name("userName")
                .email("userName@ya.ru")
                .build();
        assertThrows(Exception.class, () -> userService.updateUserById(userDto, userDto.getId()),
                "Несуществующего пользователя не должно обновлять");
    }

    @Test
    void deleteUser() {
        UserDto userDto = UserDto.builder()
                .name("userName")
                .email("userName@ya.ru")
                .build();
        UserDto user = userService.createUser(userDto);

        userService.deleteUser(user.getId());
        assertThrows(Exception.class, () -> userService.findUserById(user.getId()),
                "Пользователь должен быть удален");
    }

}

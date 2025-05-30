package ru.practicum.shareit.user;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.practicum.shareit.ShareItServer;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.service.UserService;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(classes = ShareItServer.class)
public class UserServiceTest {
   @Autowired
    private UserService userService;

    @Test
    void findUsers() {
        UserDto userDto = UserDto.builder()
                .name("userName1")
                .email("userName1@ya.ru")
                .build();
        List<UserDto> users = (List<UserDto>) userService.findAllUsers();
        assertFalse(users.isEmpty(), "Пользователи не нашелись");
    }

    @Test
    void createUser() {
        UserDto userDto = UserDto.builder()
                .name("userName2")
                .email("userName2@ya.ru")
                .build();
        UserDto user = userService.createUser(userDto);
        assertTrue(user.getId() > 0, "Пользователь не создался");
    }

    @Test
    void createUserWithWrongEmail() {
        UserDto userDto = UserDto.builder()
                .name("userName2e")
                .email("userName2e@ya.ru")
                .build();
        UserDto user = userService.createUser(userDto);
        assertThrows(Exception.class, () -> userService.createUser(userDto),
                "Создался пользователь с существующим email");
    }

    @Test
    void updateUser() {
        UserDto userDto = UserDto.builder()
                .name("userName3")
                .email("userName3@ya.ru")
                .build();
        UserDto user = userService.createUser(userDto);
        user.setName("newName");
        UserDto updUser = userService.updateUserById(user, user.getId());
        assertEquals("newName", updUser.getName(), "Имя пользователя не изменилось");
    }

    @Test
    void updateUserWhoNotExist() {
        UserDto userDto = UserDto.builder()
                .name("userName4")
                .email("userName4@ya.ru")
                .build();
        assertThrows(Exception.class, () -> userService.updateUserById(userDto, userDto.getId()),
                "Несуществующего пользователя не должно обновлять");
    }

    @Test
    void deleteUser() {
        UserDto userDto = UserDto.builder()
                .name("userName5")
                .email("userName5@ya.ru")
                .build();
        UserDto user = userService.createUser(userDto);

        userService.deleteUser(user.getId());
        assertThrows(Exception.class, () -> userService.findUserById(user.getId()),
                "Пользователь должен быть удален");
    }

}

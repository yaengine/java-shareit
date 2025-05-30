package ru.practicum.shareit.user;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import ru.practicum.shareit.ShareItGateway;
import ru.practicum.shareit.user.dto.UserDto;

import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest(classes = ShareItGateway.class)
@ActiveProfiles("default")
public class UserControllerTest {

    @Autowired
    private UserController userController;

    @Test
    void createUserWithWrongEmail() {
        UserDto userDto = new UserDto(null, "userName", "userNameya.ru");
        assertThrows(Exception.class, () -> userController.createUser(userDto),
                "Не сработала валидация email");
    }

    @Test
    void createUserWithWrongName() {
        UserDto userDto = new UserDto(null, null, "userName@ya.ru");
        assertThrows(Exception.class, () -> userController.createUser(userDto),
                "Не сработала валидация name");
    }
}

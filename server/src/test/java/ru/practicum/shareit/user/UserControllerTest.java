package ru.practicum.shareit.user;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.user.dto.UserDto;

import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
public class UserControllerTest {
    @Mock
    private UserClient userClient;

    @InjectMocks
    private UserController userController;

    @Test
    void createUserWithWrongEmail() {
        UserDto userDto = UserDto.builder()
                .name("userName")
                .email("userNameya.ru")
                .build();
        assertThrows(ValidationException.class, () -> userController.createUser(userDto),
                "Не сработала валидация email");
    }
}

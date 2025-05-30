package ru.practicum.shareit.user;

import org.junit.jupiter.api.Test;
import ru.practicum.shareit.user.dto.UserDto;

import static org.assertj.core.api.Assertions.assertThat;

class UserDtoTest {

    @Test
    void builderShouldCreateValidUserDto() {
        UserDto userDto = UserDto.builder()
                .id(1L)
                .name("userName")
                .email("userName@example.com")
                .build();

        assertThat(userDto.getId()).isEqualTo(1L);
        assertThat(userDto.getName()).isEqualTo("userName");
        assertThat(userDto.getEmail()).isEqualTo("userName@example.com");
    }

    @Test
    void equalsAndHashCodeShouldWorkCorrectly() {
        UserDto user1 = UserDto.builder()
                .id(1L)
                .name("User")
                .email("user@example.com")
                .build();

        UserDto user2 = UserDto.builder()
                .id(1L)
                .name("User")
                .email("user@example.com")
                .build();

        UserDto user3 = UserDto.builder()
                .id(2L)
                .build();

        assertThat(user1).isEqualTo(user2);
        assertThat(user1.hashCode()).isEqualTo(user2.hashCode());
        assertThat(user1).isNotEqualTo(user3);
    }

    @Test
    void toStringShouldContainImportantInfo() {
        UserDto userDto = UserDto.builder()
                .id(3L)
                .name("Test User")
                .build();

        String toString = userDto.toString();

        assertThat(toString).contains("UserDto");
        assertThat(toString).contains("id=3");
        assertThat(toString).contains("name=Test User");
    }

    @Test
    void noArgsConstructorShouldCreateEmptyDto() {
        UserDto userDto = UserDto.builder().build();

        assertThat(userDto.getId()).isNull();
        assertThat(userDto.getName()).isNull();
        assertThat(userDto.getEmail()).isNull();
    }
}

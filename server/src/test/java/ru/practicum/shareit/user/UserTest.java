package ru.practicum.shareit.user;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class UserTest {
    @Test
    void testUserBuilder() {
        User user = User.builder()
                .id(1L)
                .name("username")
                .email("username@yandex.com")
                .build();
        assertThat(user.getId()).isEqualTo(1L);
        assertThat(user.getName()).isEqualTo("username");
        assertThat(user.getEmail()).isEqualTo("username@yandex.com");
    }
}

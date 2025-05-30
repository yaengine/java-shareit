package ru.practicum.shareit.constant;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

class ConstantsTest {

    @Test
    void constantsShouldHaveCorrectValues() {

        assertEquals("X-Sharer-User-Id", Constants.X_SHARER_USER_ID);
        assertEquals("Пользователь с id %d не найден", Constants.USER_NOT_FOUND_ERR);
        assertEquals("Пользователь с email %s уже существует", Constants.USER_WITH_SAME_EMAIL_ERR);
        assertEquals("Бронирование с id %d не найдено", Constants.BOOKING_NOT_FOUND_ERR);
        assertEquals("Вещь с id %d не найдена", Constants.ITEM_NOT_FOUND_ERR);
        assertEquals("Запрос с id %d не найден", Constants.ITEM_REQUEST_NOT_FOUND_ERR);
    }

    @Test
    void errorMessagesShouldContainCorrectPlaceholders() {
        String userNotFound = String.format(Constants.USER_NOT_FOUND_ERR, 1);
        assertEquals("Пользователь с id 1 не найден", userNotFound);

        String emailExists = String.format(Constants.USER_WITH_SAME_EMAIL_ERR, "test@test.com");
        assertEquals("Пользователь с email test@test.com уже существует", emailExists);

        String bookingNotFound = String.format(Constants.BOOKING_NOT_FOUND_ERR, 10);
        assertEquals("Бронирование с id 10 не найдено", bookingNotFound);

        String itemNotFound = String.format(Constants.ITEM_NOT_FOUND_ERR, 5);
        assertEquals("Вещь с id 5 не найдена", itemNotFound);

        String requestNotFound = String.format(Constants.ITEM_REQUEST_NOT_FOUND_ERR, 3);
        assertEquals("Запрос с id 3 не найден", requestNotFound);
    }
}

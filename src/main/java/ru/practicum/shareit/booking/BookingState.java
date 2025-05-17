package ru.practicum.shareit.booking;

import lombok.AllArgsConstructor;
import lombok.Getter;

public enum BookingState {
    ALL,
    CURRENT,
    PAST,
    FUTURE,
    WAITING,
    REJECTED;
}

package ru.practicum.shareit.booking.service;

import ru.practicum.shareit.booking.BookingState;
import ru.practicum.shareit.booking.dto.BookingCreateDto;
import ru.practicum.shareit.booking.dto.BookingDto;

import java.util.List;

public interface BookingService {

    BookingDto findBookingById(Long userId, Long bookingId);

    BookingDto createBooking(Long userId, BookingCreateDto bookingCreateDto);

    BookingDto updateBookingStatus(Long userId, Long bookingId, Boolean isApproved);

    List<BookingDto> findBookingsByBookerId(Long bookerId, BookingState state);

    List<BookingDto> findBookingsByOwnerId(Long ownerId, BookingState state);
}

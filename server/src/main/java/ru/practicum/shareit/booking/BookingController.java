package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingCreateDto;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.service.BookingService;

import java.util.List;

import static ru.practicum.shareit.constant.Constants.X_SHARER_USER_ID;

@RestController
@RequestMapping(path = "/bookings")
@RequiredArgsConstructor
public class BookingController {
    private final BookingService bookingService;

    @PostMapping
    public BookingDto createBooking(@RequestHeader(X_SHARER_USER_ID) Long userId,
                                    @RequestBody BookingCreateDto bookingCreateDto) {
        return bookingService.createBooking(userId, bookingCreateDto);
    }

    @PatchMapping("/{bookingId}")
    public BookingDto updateBookingStatus(@RequestHeader(X_SHARER_USER_ID) Long userId,
                                          @PathVariable Long bookingId,
                                          @RequestParam("approved") Boolean isApproved) {
        return bookingService.updateBookingStatus(userId, bookingId, isApproved);
    }

    @GetMapping("/{bookingId}")
    public BookingDto getBookingById(@RequestHeader(X_SHARER_USER_ID) Long userId,
                                     @PathVariable Long bookingId) {
        return bookingService.findBookingById(userId, bookingId);
    }

    @GetMapping()
    public List<BookingDto> getBookingsByBookerId(@RequestHeader(X_SHARER_USER_ID) Long userId,
                                                  @RequestParam(value = "state",
                                                         required = false,
                                                         defaultValue = "ALL") BookingState state) {
        return bookingService.findBookingsByBookerId(userId, state);
    }

    @GetMapping("/owner")
    public List<BookingDto> getBookingsByOwnerId(@RequestHeader(X_SHARER_USER_ID) Long userId,
                                                  @RequestParam(value = "state",
                                                          required = false,
                                                          defaultValue = "ALL") BookingState state) {
        return bookingService.findBookingsByOwnerId(userId, state);
    }
}

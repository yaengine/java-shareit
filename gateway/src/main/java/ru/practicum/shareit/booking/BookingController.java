package ru.practicum.shareit.booking;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ru.practicum.shareit.booking.dto.BookItemRequestDto;
import ru.practicum.shareit.booking.dto.BookingState;

import static ru.practicum.shareit.contant.Constants.X_SHARER_USER_ID;


@Controller
@RequestMapping(path = "/bookings")
@RequiredArgsConstructor
@Slf4j
@Validated
public class BookingController {
	private final BookingClient bookingClient;

	@GetMapping
	public ResponseEntity<Object> getBookings(@RequestHeader(X_SHARER_USER_ID) long userId,
			@RequestParam(name = "state", defaultValue = "ALL") String stateParam) {
		BookingState state = BookingState.from(stateParam)
				.orElseThrow(() -> new IllegalArgumentException("Unknown state: " + stateParam));
		log.info("Get booking with state {}, userId={}", stateParam, userId);
		return bookingClient.getBookings(userId, state);
	}

	@PostMapping
	public ResponseEntity<Object> bookItem(@RequestHeader(X_SHARER_USER_ID) long userId,
			@RequestBody @Valid BookItemRequestDto requestDto) {
		log.info("Creating booking {}, userId={}", requestDto, userId);
		return bookingClient.bookItem(userId, requestDto);
	}

	@GetMapping("/{bookingId}")
	public ResponseEntity<Object> getBooking(@RequestHeader(X_SHARER_USER_ID) long userId,
			@PathVariable Long bookingId) {
		log.info("Get booking {}, userId={}", bookingId, userId);
		return bookingClient.getBooking(userId, bookingId);
	}

	@PatchMapping("/{bookingId}")
	public ResponseEntity<Object> updateBookingStatus(@RequestHeader(X_SHARER_USER_ID) Long userId,
										  @PathVariable Long bookingId,
										  @RequestParam("approved") Boolean isApproved) {
		return bookingClient.updateBookingStatus(userId, bookingId, isApproved);
	}

	@GetMapping("/owner")
	public ResponseEntity<Object> getBookingsByOwnerId(@RequestHeader(X_SHARER_USER_ID) Long userId,
												 		@RequestParam(value = "state",
														 required = false,
														 defaultValue = "ALL") BookingState state) {
		return bookingClient.getBookingsByOwnerId(userId, state);
	}
}

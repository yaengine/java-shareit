package ru.practicum.shareit.booking.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.booking.model.Booking;

import java.util.List;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {

    List<Booking> findByBookerId(Long bookerId);

    List<Booking> findByItemOwnerId(Long itemOwnerId);

    List<Booking> findByBookerIdAndItemId(Long bookerId, Long itemId);
}

package ru.practicum.shareit.item.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.booking.dto.BookingDto;

import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ItemDto {
    Long id;
    @NotBlank(message = "Название не должно быть пустым")
    String name;
    @NotBlank(message = "Описание не должно быть пустым")
    String description;
    @NotNull(message = "Вещь должна быть либо доступна для аренды либо нет")
    Boolean available;
    Long requestId;
    BookingDto lastBooking;
    BookingDto nextBooking;
    List<CommentDto> comments;
}

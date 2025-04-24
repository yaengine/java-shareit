package ru.practicum.shareit.item.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import ru.practicum.shareit.request.ItemRequest;
import ru.practicum.shareit.user.User;

/**
 * TODO Sprint add-controllers.
 */
@AllArgsConstructor
@Data
public class ItemDto {
    Long id;
    @NotBlank(message = "Название не должно быть пустым")
    String name;
    @NotBlank(message = "Описание не должно быть пустым")
    String description;
    @NotBlank(message = "Вещь должна быть либо доступна для аренды либо нет")
    Boolean available;
    Long requestId;
}

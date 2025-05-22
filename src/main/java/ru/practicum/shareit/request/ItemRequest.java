package ru.practicum.shareit.request;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Data;
import ru.practicum.shareit.user.User;

import java.time.LocalDateTime;

@Data
@Builder
@Entity
@Table(name = "requests")
public class ItemRequest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    String description;
    @ManyToOne
    @JoinColumn(name = "requestor_id")
    User requestor;
    LocalDateTime created;
}

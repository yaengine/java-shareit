package ru.practicum.shareit.user;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Builder
@Data
@Table(name = "users")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    String name;
    String email;
}

package ru.job4j.socialmedia.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(
        name = "friendship",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"owner_id", "friend_id"})
        }
)
@Data
@NoArgsConstructor
public class Friendship {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "owner_id")
    private Account owner;

    @ManyToOne(optional = false)
    @JoinColumn(name = "friend_id")
    private Account friend;

    @Column(nullable = false)
    private LocalDateTime created;

}

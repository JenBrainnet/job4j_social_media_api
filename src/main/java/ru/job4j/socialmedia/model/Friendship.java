package ru.job4j.socialmedia.model;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;
import java.time.ZoneOffset;

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
    @DateTimeFormat(pattern = "dd-MM-yyyy HH:mm")
    private LocalDateTime created = LocalDateTime.now(ZoneOffset.UTC);

}

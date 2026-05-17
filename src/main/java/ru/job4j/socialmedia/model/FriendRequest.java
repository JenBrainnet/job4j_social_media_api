package ru.job4j.socialmedia.model;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;
import java.time.ZoneOffset;

@Entity
@Table(
        name = "friend_request",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"sender_id", "receiver_id"})
        }
)
@Data
@NoArgsConstructor
public class FriendRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "sender_id")
    private Account sender;

    @ManyToOne(optional = false)
    @JoinColumn(name = "receiver_id")
    private Account receiver;

    @Column(nullable = false)
    private String status;

    @Column(nullable = false)
    @DateTimeFormat(pattern = "dd-MM-yyyy HH:mm")
    private LocalDateTime created = LocalDateTime.now(ZoneOffset.UTC);

}

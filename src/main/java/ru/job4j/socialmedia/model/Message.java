package ru.job4j.socialmedia.model;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;
import java.time.ZoneOffset;

@Entity
@Table(name = "message")
@Data
@NoArgsConstructor
public class Message {

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
    private String text;

    @Column(nullable = false)
    @DateTimeFormat(pattern = "dd-MM-yyyy HH:mm")
    private LocalDateTime created = LocalDateTime.now(ZoneOffset.UTC);

}

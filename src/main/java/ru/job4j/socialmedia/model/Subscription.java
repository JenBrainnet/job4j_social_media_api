package ru.job4j.socialmedia.model;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;
import java.time.ZoneOffset;

@Entity
@Table(
        name = "subscription",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"subscriber_id", "subscribed_account_id"})
        }
)
@Data
@NoArgsConstructor
public class Subscription {

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Integer id;

        @ManyToOne(optional = false)
        @JoinColumn(name = "subscriber_id")
        private Account subscriber;

        @ManyToOne(optional = false)
        @JoinColumn(name = "subscribed_account_id")
        private Account subscribedAccount;

        @Column(nullable = false)
        @DateTimeFormat(pattern = "dd-MM-yyyy HH:mm")
        private LocalDateTime created = LocalDateTime.now(ZoneOffset.UTC);

}

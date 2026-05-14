package ru.job4j.socialmedia.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "post_image")
@Data
@NoArgsConstructor
public class PostImage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String path;

    @ManyToOne
    @JoinColumn(name = "post_id")
    private Post post;

}

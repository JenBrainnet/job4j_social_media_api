package ru.job4j.socialmedia.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.job4j.socialmedia.model.Post;

import java.time.LocalDateTime;
import java.util.List;

public interface PostRepository extends JpaRepository<Post, Integer> {

    List<Post> findByAccountId(Integer accountId);

    List<Post> findByCreatedBetween(LocalDateTime start, LocalDateTime finish);

    Page<Post> findAllByOrderByCreatedDesc(Pageable pageable);

}

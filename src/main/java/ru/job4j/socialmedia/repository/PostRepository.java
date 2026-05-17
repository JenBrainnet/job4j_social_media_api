package ru.job4j.socialmedia.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.job4j.socialmedia.model.Post;

import java.time.LocalDateTime;
import java.util.List;

public interface PostRepository extends JpaRepository<Post, Integer> {

    List<Post> findByAccountId(Integer accountId);

    List<Post> findByCreatedBetween(LocalDateTime start, LocalDateTime finish);

    Page<Post> findAllByOrderByCreatedDesc(Pageable pageable);

    @Modifying(clearAutomatically = true)
    @Query("""
            UPDATE Post post SET post.title = :title, post.text = :text
            WHERE post.id = :postId AND post.account.id = :accountId
            """
    )
    int updateTitleAndTextByIdAndAccountId(
            @Param("postId") Integer postId,
            @Param("accountId") Integer accountId,
            @Param("title") String title,
            @Param("text") String text
    );

    @Modifying(clearAutomatically = true)
    @Query("""
            DELETE FROM PostImage image
            WHERE image.id = :imageId AND image.post.id = :postId
            """
    )
    int deleteImageByIdAndPostId(
            @Param("imageId") Integer imageId,
            @Param("postId") Integer postId
    );

    @Modifying(clearAutomatically = true)
    @Query("""
        DELETE FROM PostImage image
        WHERE image.post.id = :postId AND image.post.account.id = :accountId
        """
    )
    int deleteImagesByPostIdAndAccountId(
            @Param("postId") Integer postId,
            @Param("accountId") Integer accountId
    );

    @Modifying(clearAutomatically = true)
    @Query("""
        DELETE FROM Post post
        WHERE post.id = :postId AND post.account.id = :accountId
        """
    )
    int deletePostByIdAndAccountId(
            @Param("postId") Integer postId,
            @Param("accountId") Integer accountId
    );

    @Query("""
            SELECT post FROM Post post
            WHERE post.account.id IN (
                 SELECT subscription.subscribedAccount.id FROM Subscription subscription
                 WHERE subscription.subscriber.id = :accountId
            )
            ORDER BY post.created DESC
            """
    )
    Page<Post> findFeedPostsOrderByCreatedDesc(
            @Param("accountId") Integer accountId,
            Pageable pageable
    );

}

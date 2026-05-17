package ru.job4j.socialmedia.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import ru.job4j.socialmedia.model.Account;
import ru.job4j.socialmedia.model.PostImage;
import ru.job4j.socialmedia.repository.AccountRepository;
import ru.job4j.socialmedia.repository.FriendRequestRepository;
import ru.job4j.socialmedia.repository.FriendshipRepository;
import ru.job4j.socialmedia.repository.MessageRepository;
import ru.job4j.socialmedia.repository.PostImageRepository;
import ru.job4j.socialmedia.repository.PostRepository;
import ru.job4j.socialmedia.repository.SubscriptionRepository;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ActiveProfiles("test")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class PostServiceTest {

    @Autowired
    private PostService postService;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private PostImageRepository postImageRepository;

    @Autowired
    private SubscriptionRepository subscriptionRepository;

    @Autowired
    private FriendRequestRepository friendRequestRepository;

    @Autowired
    private FriendshipRepository friendshipRepository;

    @Autowired
    private MessageRepository messageRepository;

    @BeforeEach
    void setUp() {
        messageRepository.deleteAll();
        friendshipRepository.deleteAll();
        friendRequestRepository.deleteAll();
        subscriptionRepository.deleteAll();
        postImageRepository.deleteAll();
        postRepository.deleteAll();
        accountRepository.deleteAll();
    }

    @Test
    void whenCreatePostWithImagesThenSavePostAndImages() {
        var account = accountRepository.save(createAccount("john", "john@example.com"));
        var firstImage = createImage("first.jpg", "files/first.jpg");
        var secondImage = createImage("second.jpg", "files/second.jpg");
        var result = postService.create(
                account.getId(),
                "title",
                "text",
                List.of(firstImage, secondImage)
        );
        assertThat(result).isPresent();
        assertThat(postRepository.findById(result.get().getId())).isPresent();
        assertThat(postImageRepository.findAll())
                .hasSize(2)
                .extracting(PostImage::getName)
                .contains("first.jpg", "second.jpg");
    }

    @Test
    void whenUpdateOwnPostThenReturnTrueAndUpdate() {
        var account = accountRepository.save(createAccount("john", "john@example.com"));
        var post = postService.create(account.getId(), "old", "old text", List.of()).orElseThrow();
        var result = postService.update(account.getId(), post.getId(), "new", "new text");
        var updated = postRepository.findById(post.getId()).orElseThrow();
        assertThat(result).isTrue();
        assertThat(updated.getTitle()).isEqualTo("new");
        assertThat(updated.getText()).isEqualTo("new text");
    }

    @Test
    void whenUpdateAnotherAccountPostThenReturnFalse() {
        var owner = accountRepository.save(createAccount("john", "john@example.com"));
        var another = accountRepository.save(createAccount("jane", "jane@example.com"));
        var post = postService.create(owner.getId(), "old", "old text", List.of()).orElseThrow();
        var result = postService.update(another.getId(), post.getId(), "new", "new text");
        var notUpdated = postRepository.findById(post.getId()).orElseThrow();
        assertThat(result).isFalse();
        assertThat(notUpdated.getTitle()).isEqualTo("old");
    }

    @Test
    void whenDeleteOwnPostThenDeletePostAndImages() {
        var account = accountRepository.save(createAccount("john", "john@example.com"));
        var image = createImage("first.jpg", "files/first.jpg");
        var post = postService.create(account.getId(), "title", "text", List.of(image)).orElseThrow();
        var result = postService.delete(account.getId(), post.getId());
        assertThat(result).isTrue();
        assertThat(postRepository.findById(post.getId())).isEmpty();
        assertThat(postImageRepository.findAll()).isEmpty();
    }

    private PostImage createImage(String name, String path) {
        var image = new PostImage();
        image.setName(name);
        image.setPath(path);
        return image;
    }

    private Account createAccount(String username, String email) {
        var account = new Account();
        account.setUsername(username);
        account.setEmail(email);
        account.setPassword("password");
        account.setTimeZone("UTC");
        account.setCreated(LocalDateTime.now());
        return account;
    }

}

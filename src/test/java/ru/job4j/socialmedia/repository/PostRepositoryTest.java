package ru.job4j.socialmedia.repository;

import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import ru.job4j.socialmedia.model.Account;
import ru.job4j.socialmedia.model.Post;
import ru.job4j.socialmedia.model.PostImage;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class PostRepositoryTest {

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
    void whenSavePostThenFindById() {
        var account = accountRepository.save(createAccount("john", "john@example.com"));
        var post = new Post();
        post.setTitle("First post");
        post.setText("Post text");
        post.setCreated(LocalDateTime.now());
        post.setAccount(account);
        postRepository.save(post);
        var foundPost = postRepository.findById(post.getId());
        assertThat(foundPost).isPresent();
        assertThat(foundPost.get().getTitle()).isEqualTo("First post");
        assertThat(foundPost.get().getAccount().getUsername()).isEqualTo("john");
    }

    @Test
    @Transactional
    void whenSavePostWithImagesThenFindById() {
        var account = accountRepository.save(createAccount("john-images", "john-images@example.com"));
        var post = new Post();
        post.setTitle("Post with image");
        post.setText("Post text");
        post.setCreated(LocalDateTime.now());
        post.setAccount(account);
        var image = new PostImage();
        image.setName("image.jpg");
        image.setPath("files/image.jpg");
        image.setPost(post);
        post.getImages().add(image);
        postRepository.save(post);
        var foundPost = postRepository.findById(post.getId());
        assertThat(foundPost).isPresent();
        assertThat(foundPost.get().getImages()).hasSize(1);
        assertThat(foundPost.get().getImages().getFirst().getName()).isEqualTo("image.jpg");
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
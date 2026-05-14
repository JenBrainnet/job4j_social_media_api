package ru.job4j.socialmedia.repository;

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
class PostImageRepositoryTest {

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
    void whenSavePostWithImagesThenFindAllImages() {
        var account = accountRepository.save(createAccount("john-post-image", "john-post-image@example.com"));
        var post = new Post();
        post.setTitle("Post with images");
        post.setText("Post text");
        post.setCreated(LocalDateTime.now());
        post.setAccount(account);
        var firstImage = new PostImage();
        firstImage.setName("first.jpg");
        firstImage.setPath("files/first.jpg");
        firstImage.setPost(post);
        var secondImage = new PostImage();
        secondImage.setName("second.jpg");
        secondImage.setPath("files/second.jpg");
        secondImage.setPost(post);
        post.getImages().add(firstImage);
        post.getImages().add(secondImage);
        postRepository.save(post);
        var images = postImageRepository.findAll();
        assertThat(images).hasSize(2);
        assertThat(images)
                .extracting(PostImage::getName)
                .contains("first.jpg", "second.jpg");
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
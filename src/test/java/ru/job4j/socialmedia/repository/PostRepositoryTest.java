package ru.job4j.socialmedia.repository;

import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
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
        var post = createPost("First post", account, LocalDateTime.now());
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
        var post = createPost("Post with image", account, LocalDateTime.now());
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

    @Test
    void whenFindByAccountIdThenReturnAccountPosts() {
        var firstAccount = accountRepository.save(createAccount("john-posts", "john-posts@example.com"));
        var secondAccount = accountRepository.save(createAccount("jane-posts", "jane-posts@example.com"));
        postRepository.save(createPost(
                "First post",
                firstAccount,
                LocalDateTime.of(2024, 1, 1, 10, 0)
        ));
        postRepository.save(createPost(
                "Second post",
                firstAccount,
                LocalDateTime.of(2024, 1, 2, 10, 0)
        ));
        postRepository.save(createPost(
                "Other post",
                secondAccount,
                LocalDateTime.of(2024, 1, 3, 10, 0)
        ));
        var posts = postRepository.findByAccountId(firstAccount.getId());
        assertThat(posts).hasSize(2);
        assertThat(posts)
                .extracting(Post::getTitle)
                .contains("First post", "Second post");
    }

    @Test
    void whenFindByCreatedBetweenThenReturnPostsInDateRange() {
        var account = accountRepository.save(createAccount("john-dates", "john-dates@example.com"));
        postRepository.save(createPost(
                "Old post",
                account,
                LocalDateTime.of(2024, 1, 1, 10, 0)
        ));
        postRepository.save(createPost(
                "Middle post",
                account,
                LocalDateTime.of(2024, 2, 1, 10, 0)
        ));
        postRepository.save(createPost(
                "New post",
                account,
                LocalDateTime.of(2024, 3, 1, 10, 0)
        ));
        var posts = postRepository.findByCreatedBetween(
                LocalDateTime.of(2024, 1, 15, 0, 0),
                LocalDateTime.of(2024, 2, 15, 23, 59)
        );
        assertThat(posts).hasSize(1);
        assertThat(posts.getFirst().getTitle()).isEqualTo("Middle post");
    }

    @Test
    void whenFindAllByOrderByCreatedDescThenReturnSortedPage() {
        var account = accountRepository.save(createAccount("john-page", "john-page@example.com"));
        postRepository.save(createPost(
                "Old post",
                account,
                LocalDateTime.of(2024, 1, 1, 10, 0)
        ));
        postRepository.save(createPost(
                "Middle post",
                account,
                LocalDateTime.of(2024, 2, 1, 10, 0)
        ));
        postRepository.save(createPost(
                "New post",
                account,
                LocalDateTime.of(2024, 3, 1, 10, 0)
        ));
        var page = postRepository.findAllByOrderByCreatedDesc(PageRequest.of(0, 2));
        assertThat(page.getContent()).hasSize(2);
        assertThat(page.getTotalPages()).isEqualTo(2);
        assertThat(page.getContent())
                .extracting(Post::getTitle)
                .containsExactly("New post", "Middle post");
    }

    private Post createPost(String title, Account account, LocalDateTime created) {
        var post = new Post();
        post.setTitle(title);
        post.setText("Post text");
        post.setCreated(created);
        post.setAccount(account);
        return post;
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
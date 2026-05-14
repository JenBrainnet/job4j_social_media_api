package ru.job4j.socialmedia.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import ru.job4j.socialmedia.model.Account;
import ru.job4j.socialmedia.model.Subscription;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class SubscriptionRepositoryTest {

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
    void whenSaveSubscriptionThenFindById() {
        var subscriber = accountRepository.save(createAccount("john", "john@example.com"));
        var subscribedAccount = accountRepository.save(createAccount("jane", "jane@example.com"));
        var subscription = new Subscription();
        subscription.setSubscriber(subscriber);
        subscription.setSubscribedAccount(subscribedAccount);
        subscription.setCreated(LocalDateTime.now());
        subscriptionRepository.save(subscription);
        var foundSubscription = subscriptionRepository.findById(subscription.getId());
        assertThat(foundSubscription).isPresent();
        assertThat(foundSubscription.get().getSubscriber().getUsername()).isEqualTo("john");
        assertThat(foundSubscription.get().getSubscribedAccount().getUsername()).isEqualTo("jane");
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
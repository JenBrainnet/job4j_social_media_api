package ru.job4j.socialmedia.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import ru.job4j.socialmedia.model.Account;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class AccountRepositoryTest {

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
    void whenSaveAccountThenFindById() {
        var account = createAccount("john", "john@example.com");
        accountRepository.save(account);
        var foundAccount = accountRepository.findById(account.getId());
        assertThat(foundAccount).isPresent();
        assertThat(foundAccount.get().getUsername()).isEqualTo("john");
    }

    @Test
    void whenFindAllAccountsThenReturnAllAccounts() {
        accountRepository.save(createAccount("john", "john@example.com"));
        accountRepository.save(createAccount("jane", "jane@example.com"));
        var accounts = accountRepository.findAll();
        assertThat(accounts).hasSize(2);
        assertThat(accounts)
                .extracting(Account::getUsername)
                .contains("john", "jane");
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
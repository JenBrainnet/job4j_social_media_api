package ru.job4j.socialmedia.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import ru.job4j.socialmedia.model.Account;
import ru.job4j.socialmedia.model.Friendship;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class FriendshipRepositoryTest {

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
    void whenSaveFriendshipThenFindById() {
        var owner = accountRepository.save(createAccount("john", "john@example.com"));
        var friend = accountRepository.save(createAccount("jane", "jane@example.com"));
        var friendship = new Friendship();
        friendship.setOwner(owner);
        friendship.setFriend(friend);
        friendship.setCreated(LocalDateTime.now());
        friendshipRepository.save(friendship);
        var foundFriendship = friendshipRepository.findById(friendship.getId());
        assertThat(foundFriendship).isPresent();
        assertThat(foundFriendship.get().getOwner().getUsername()).isEqualTo("john");
        assertThat(foundFriendship.get().getFriend().getUsername()).isEqualTo("jane");
    }

    @Test
    void whenFindAllFriendshipsThenReturnAllFriendships() {
        var firstOwner = accountRepository.save(createAccount("john", "john@example.com"));
        var firstFriend = accountRepository.save(createAccount("jane", "jane@example.com"));
        var secondOwner = accountRepository.save(createAccount("bob", "bob@example.com"));
        var secondFriend = accountRepository.save(createAccount("alice", "alice@example.com"));
        friendshipRepository.save(createFriendship(firstOwner, firstFriend));
        friendshipRepository.save(createFriendship(secondOwner, secondFriend));
        var friendships = friendshipRepository.findAll();
        assertThat(friendships).hasSize(2);
        assertThat(friendships)
                .extracting(friendship -> friendship.getOwner().getUsername())
                .contains("john", "bob");
    }

    private Friendship createFriendship(Account owner, Account friend) {
        var friendship = new Friendship();
        friendship.setOwner(owner);
        friendship.setFriend(friend);
        friendship.setCreated(LocalDateTime.now());
        return friendship;
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
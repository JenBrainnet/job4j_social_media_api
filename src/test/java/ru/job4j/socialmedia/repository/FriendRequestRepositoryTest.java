package ru.job4j.socialmedia.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import ru.job4j.socialmedia.model.Account;
import ru.job4j.socialmedia.model.FriendRequest;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class FriendRequestRepositoryTest {

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
    void whenSaveFriendRequestThenFindById() {
        var sender = accountRepository.save(createAccount("john", "john@example.com"));
        var receiver = accountRepository.save(createAccount("jane", "jane@example.com"));
        var friendRequest = new FriendRequest();
        friendRequest.setSender(sender);
        friendRequest.setReceiver(receiver);
        friendRequest.setStatus("NEW");
        friendRequest.setCreated(LocalDateTime.now());
        friendRequestRepository.save(friendRequest);
        var foundFriendRequest = friendRequestRepository.findById(friendRequest.getId());
        assertThat(foundFriendRequest).isPresent();
        assertThat(foundFriendRequest.get().getSender().getUsername()).isEqualTo("john");
        assertThat(foundFriendRequest.get().getReceiver().getUsername()).isEqualTo("jane");
        assertThat(foundFriendRequest.get().getStatus()).isEqualTo("NEW");
    }

    @Test
    void whenFindAllFriendRequestsThenReturnAllFriendRequests() {
        var firstSender = accountRepository.save(createAccount("john", "john@example.com"));
        var firstReceiver = accountRepository.save(createAccount("jane", "jane@example.com"));
        var secondSender = accountRepository.save(createAccount("bob", "bob@example.com"));
        var secondReceiver = accountRepository.save(createAccount("alice", "alice@example.com"));
        friendRequestRepository.save(createFriendRequest(firstSender, firstReceiver));
        friendRequestRepository.save(createFriendRequest(secondSender, secondReceiver));
        var friendRequests = friendRequestRepository.findAll();
        assertThat(friendRequests).hasSize(2);
        assertThat(friendRequests)
                .extracting(FriendRequest::getStatus)
                .contains("NEW");
    }

    private FriendRequest createFriendRequest(Account sender, Account receiver) {
        var friendRequest = new FriendRequest();
        friendRequest.setSender(sender);
        friendRequest.setReceiver(receiver);
        friendRequest.setStatus("NEW");
        friendRequest.setCreated(LocalDateTime.now());
        return friendRequest;
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
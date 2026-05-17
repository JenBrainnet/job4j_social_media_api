package ru.job4j.socialmedia.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import ru.job4j.socialmedia.model.Account;
import ru.job4j.socialmedia.repository.AccountRepository;
import ru.job4j.socialmedia.repository.FriendRequestRepository;
import ru.job4j.socialmedia.repository.FriendshipRepository;
import ru.job4j.socialmedia.repository.MessageRepository;
import ru.job4j.socialmedia.repository.PostImageRepository;
import ru.job4j.socialmedia.repository.PostRepository;
import ru.job4j.socialmedia.repository.SubscriptionRepository;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ActiveProfiles("test")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class SubscribeServiceTest {

    @Autowired
    private SubscribeService subscribeService;

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
    void whenSendFriendRequestThenSenderSubscribesToReceiver() {
        var sender = accountRepository.save(createAccount("john", "john@example.com"));
        var receiver = accountRepository.save(createAccount("jane", "jane@example.com"));
        var request = subscribeService.sendFriendRequest(sender.getId(), receiver.getId());
        assertThat(request).isPresent();
        assertThat(request.get().getStatus()).isEqualTo("PENDING");
        assertThat(subscriptionRepository.existsBySubscriberIdAndSubscribedAccountId(
                sender.getId(), receiver.getId()
        )).isTrue();
    }

    @Test
    void whenAcceptFriendRequestThenBothAccountsBecomeFriendsAndSubscribers() {
        var sender = accountRepository.save(createAccount("john", "john@example.com"));
        var receiver = accountRepository.save(createAccount("jane", "jane@example.com"));
        subscribeService.sendFriendRequest(sender.getId(), receiver.getId());
        var result = subscribeService.acceptFriendRequest(sender.getId(), receiver.getId());
        assertThat(result).isTrue();
        assertThat(friendshipRepository.existsByOwnerIdAndFriendId(sender.getId(), receiver.getId())).isTrue();
        assertThat(friendshipRepository.existsByOwnerIdAndFriendId(receiver.getId(), sender.getId())).isTrue();
        assertThat(subscriptionRepository.existsBySubscriberIdAndSubscribedAccountId(
                sender.getId(), receiver.getId()
        )).isTrue();
        assertThat(subscriptionRepository.existsBySubscriberIdAndSubscribedAccountId(
                receiver.getId(), sender.getId()
        )).isTrue();
    }

    @Test
    void whenRejectFriendRequestThenSenderStillSubscribed() {
        var sender = accountRepository.save(createAccount("john", "john@example.com"));
        var receiver = accountRepository.save(createAccount("jane", "jane@example.com"));
        subscribeService.sendFriendRequest(sender.getId(), receiver.getId());
        var result = subscribeService.rejectFriendRequest(sender.getId(), receiver.getId());
        assertThat(result).isTrue();
        assertThat(friendshipRepository.existsByOwnerIdAndFriendId(sender.getId(), receiver.getId())).isFalse();
        assertThat(subscriptionRepository.existsBySubscriberIdAndSubscribedAccountId(
                sender.getId(), receiver.getId()
        )).isTrue();
    }

    @Test
    void whenUnsubscribeThenSubscriptionDeleted() {
        var sender = accountRepository.save(createAccount("john", "john@example.com"));
        var receiver = accountRepository.save(createAccount("jane", "jane@example.com"));
        subscribeService.sendFriendRequest(sender.getId(), receiver.getId());
        var result = subscribeService.unsubscribe(sender.getId(), receiver.getId());
        assertThat(result).isTrue();
        assertThat(subscriptionRepository.existsBySubscriberIdAndSubscribedAccountId(
                sender.getId(), receiver.getId()
        )).isFalse();
    }

    @Test
    void whenRemoveFriendThenOwnerUnsubscribesAndSecondUserStaysSubscriber() {
        var owner = accountRepository.save(createAccount("john", "john@example.com"));
        var friend = accountRepository.save(createAccount("jane", "jane@example.com"));
        subscribeService.sendFriendRequest(owner.getId(), friend.getId());
        subscribeService.acceptFriendRequest(owner.getId(), friend.getId());
        var result = subscribeService.removeFriend(owner.getId(), friend.getId());
        assertThat(result).isTrue();
        assertThat(friendshipRepository.existsByOwnerIdAndFriendId(owner.getId(), friend.getId())).isFalse();
        assertThat(friendshipRepository.existsByOwnerIdAndFriendId(friend.getId(), owner.getId())).isFalse();
        assertThat(subscriptionRepository.existsBySubscriberIdAndSubscribedAccountId(
                owner.getId(), friend.getId()
        )).isFalse();
        assertThat(subscriptionRepository.existsBySubscriberIdAndSubscribedAccountId(
                friend.getId(), owner.getId()
        )).isTrue();
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

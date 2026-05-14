package ru.job4j.socialmedia.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import ru.job4j.socialmedia.model.Account;
import ru.job4j.socialmedia.model.Message;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class MessageRepositoryTest {

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
    void whenSaveMessageThenFindById() {
        var sender = accountRepository.save(createAccount("john", "john@example.com"));
        var receiver = accountRepository.save(createAccount("jane", "jane@example.com"));
        var message = new Message();
        message.setSender(sender);
        message.setReceiver(receiver);
        message.setText("Hello");
        message.setCreated(LocalDateTime.now());
        messageRepository.save(message);
        var foundMessage = messageRepository.findById(message.getId());
        assertThat(foundMessage).isPresent();
        assertThat(foundMessage.get().getSender().getUsername()).isEqualTo("john");
        assertThat(foundMessage.get().getReceiver().getUsername()).isEqualTo("jane");
        assertThat(foundMessage.get().getText()).isEqualTo("Hello");
    }

    @Test
    void whenFindAllMessagesThenReturnAllMessages() {
        var john = accountRepository.save(createAccount("john", "john@example.com"));
        var jane = accountRepository.save(createAccount("jane", "jane@example.com"));
        var bob = accountRepository.save(createAccount("bob", "bob@example.com"));
        messageRepository.save(createMessage(john, jane, "Hello"));
        messageRepository.save(createMessage(jane, bob, "Hi"));
        var messages = messageRepository.findAll();
        assertThat(messages).hasSize(2);
        assertThat(messages)
                .extracting(Message::getText)
                .contains("Hello", "Hi");
    }

    private Message createMessage(Account sender, Account receiver, String text) {
        var message = new Message();
        message.setSender(sender);
        message.setReceiver(receiver);
        message.setText(text);
        message.setCreated(LocalDateTime.now());
        return message;
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
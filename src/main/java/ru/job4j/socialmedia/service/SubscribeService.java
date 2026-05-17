package ru.job4j.socialmedia.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.job4j.socialmedia.model.Account;
import ru.job4j.socialmedia.model.FriendRequest;
import ru.job4j.socialmedia.model.Friendship;
import ru.job4j.socialmedia.model.Subscription;
import ru.job4j.socialmedia.repository.AccountRepository;
import ru.job4j.socialmedia.repository.FriendRequestRepository;
import ru.job4j.socialmedia.repository.FriendshipRepository;
import ru.job4j.socialmedia.repository.SubscriptionRepository;

import java.util.Optional;

@Service
@AllArgsConstructor
@Slf4j
public class SubscribeService {

    private static final String PENDING = "PENDING";
    private static final String ACCEPTED = "ACCEPTED";
    private static final String REJECTED = "REJECTED";

    private final AccountRepository accountRepository;
    private final SubscriptionRepository subscriptionRepository;
    private final FriendRequestRepository friendRequestRepository;
    private final FriendshipRepository friendshipRepository;

    @Transactional
    public Optional<FriendRequest> sendFriendRequest(Integer senderId, Integer receiverId) {
        if (senderId.equals(receiverId)) {
            log.warn("Friend request was not created. Sender id={} is equal to receiver id.", senderId);
            return Optional.empty();
        }
        var senderOptional = accountRepository.findById(senderId);
        var receiverOptional = accountRepository.findById(receiverId);
        if (senderOptional.isEmpty() || receiverOptional.isEmpty()) {
            log.warn("Friend request was not created. Sender id={}, receiver id={}.", senderId, receiverId);
            return Optional.empty();
        }
        if (friendRequestRepository.existsBySenderIdAndReceiverId(senderId, receiverId)) {
            log.warn("Friend request from account id={} to account id={} already exists.", senderId, receiverId);
            return Optional.empty();
        }
        var sender = senderOptional.get();
        var receiver = receiverOptional.get();
        createSubscriptionIfNotExists(sender, receiver);
        var request = new FriendRequest();
        request.setSender(sender);
        request.setReceiver(receiver);
        request.setStatus(PENDING);
        return Optional.of(friendRequestRepository.save(request));
    }

    @Transactional
    public boolean acceptFriendRequest(Integer senderId, Integer receiverId) {
        var requestOptional = friendRequestRepository.findBySenderIdAndReceiverId(senderId, receiverId);
        if (requestOptional.isEmpty()) {
            log.warn("Friend request from account id={} to account id={} was not found.", senderId, receiverId);
            return false;
        }
        var request = requestOptional.get();
        request.setStatus(ACCEPTED);
        friendRequestRepository.save(request);
        var sender = request.getSender();
        var receiver = request.getReceiver();
        createFriendshipIfNotExists(sender, receiver);
        createFriendshipIfNotExists(receiver, sender);
        createSubscriptionIfNotExists(sender, receiver);
        createSubscriptionIfNotExists(receiver, sender);
        return true;
    }

    @Transactional
    public boolean rejectFriendRequest(Integer senderId, Integer receiverId) {
        var requestOptional = friendRequestRepository.findBySenderIdAndReceiverId(senderId, receiverId);
        if (requestOptional.isEmpty()) {
            log.warn("Friend request from account id={} to account id={} was not found.", senderId, receiverId);
            return false;
        }
        var request = requestOptional.get();
        request.setStatus(REJECTED);
        friendRequestRepository.save(request);
        return true;
    }

    @Transactional
    public boolean removeFriend(Integer ownerId, Integer friendId) {
        if (ownerId.equals(friendId)) {
            log.warn("Friend was not removed. Owner id={} is equal to friend id.", ownerId);
            return false;
        }
        friendshipRepository.deleteByOwnerIdAndFriendId(ownerId, friendId);
        friendshipRepository.deleteByOwnerIdAndFriendId(friendId, ownerId);
        subscriptionRepository.deleteBySubscriberIdAndSubscribedAccountId(ownerId, friendId);
        return true;
    }

    @Transactional
    public boolean unsubscribe(Integer subscriberId, Integer subscribedAccountId) {
        if (!subscriptionRepository.existsBySubscriberIdAndSubscribedAccountId(
                subscriberId, subscribedAccountId
        )) {
            return false;
        }
        subscriptionRepository.deleteBySubscriberIdAndSubscribedAccountId(subscriberId, subscribedAccountId);
        return true;
    }

    private void createSubscriptionIfNotExists(Account subscriber, Account subscribedAccount) {
        if (subscriptionRepository.existsBySubscriberIdAndSubscribedAccountId(
                subscriber.getId(), subscribedAccount.getId()
        )) {
            return;
        }
        var subscription = new Subscription();
        subscription.setSubscriber(subscriber);
        subscription.setSubscribedAccount(subscribedAccount);
        subscriptionRepository.save(subscription);
    }

    private void createFriendshipIfNotExists(Account owner, Account friend) {
        if (friendshipRepository.existsByOwnerIdAndFriendId(owner.getId(), friend.getId())) {
            return;
        }
        var friendship = new Friendship();
        friendship.setOwner(owner);
        friendship.setFriend(friend);
        friendshipRepository.save(friendship);
    }

}

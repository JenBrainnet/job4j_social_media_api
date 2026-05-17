package ru.job4j.socialmedia.repository;

import org.springframework.data.repository.CrudRepository;
import ru.job4j.socialmedia.model.FriendRequest;

import java.util.Optional;

public interface FriendRequestRepository extends CrudRepository<FriendRequest, Integer> {

    Optional<FriendRequest> findBySenderIdAndReceiverId(Integer senderId, Integer receiverId);

    boolean existsBySenderIdAndReceiverId(Integer senderId, Integer receiverId);

}

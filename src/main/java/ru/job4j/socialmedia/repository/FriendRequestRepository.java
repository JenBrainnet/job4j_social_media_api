package ru.job4j.socialmedia.repository;

import org.springframework.data.repository.CrudRepository;
import ru.job4j.socialmedia.model.FriendRequest;

public interface FriendRequestRepository extends CrudRepository<FriendRequest, Integer> {
}

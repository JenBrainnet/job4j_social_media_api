package ru.job4j.socialmedia.repository;

import org.springframework.data.repository.CrudRepository;
import ru.job4j.socialmedia.model.Friendship;

import java.util.Optional;

public interface FriendshipRepository extends CrudRepository<Friendship, Integer> {

    Optional<Friendship> findByOwnerIdAndFriendId(Integer ownerId, Integer friendId);

    boolean existsByOwnerIdAndFriendId(Integer ownerId, Integer friendId);

    void deleteByOwnerIdAndFriendId(Integer ownerId, Integer friendId);

}

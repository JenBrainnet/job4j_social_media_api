package ru.job4j.socialmedia.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.job4j.socialmedia.model.Account;

import java.util.List;
import java.util.Optional;

public interface AccountRepository extends JpaRepository<Account, Integer> {

    @Query("""
            SELECT account FROM Account account
            WHERE account.username = ?1 AND account.password = ?2
            """
    )
    Optional<Account> findByUsernameAndPassword(String username, String password);

    @Query("""
            SELECT subscription.subscriber FROM Subscription subscription
            WHERE subscription.subscribedAccount.id = ?1
            """
    )
    List<Account> findSubscribersByAccountId(Integer accountId);

    @Query("""
            SELECT friendship.friend FROM Friendship friendship
            WHERE friendship.owner.id = ?1
            """
    )
    List<Account> findFriendsByAccountId(Integer accountId);

}

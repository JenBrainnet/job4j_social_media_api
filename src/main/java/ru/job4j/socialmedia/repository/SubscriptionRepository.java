package ru.job4j.socialmedia.repository;

import org.springframework.data.repository.CrudRepository;
import ru.job4j.socialmedia.model.Subscription;

import java.util.Optional;

public interface SubscriptionRepository extends CrudRepository<Subscription, Integer> {

    Optional<Subscription> findBySubscriberIdAndSubscribedAccountId(
            Integer subscriberId,
            Integer subscribedAccountId
    );

    boolean existsBySubscriberIdAndSubscribedAccountId(
            Integer subscriberId,
            Integer subscribedAccountId
    );

    void deleteBySubscriberIdAndSubscribedAccountId(
            Integer subscriberId,
            Integer subscribedAccountId
    );

}

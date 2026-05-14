package ru.job4j.socialmedia.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.job4j.socialmedia.model.Account;

public interface AccountRepository extends JpaRepository<Account, Integer> {
}

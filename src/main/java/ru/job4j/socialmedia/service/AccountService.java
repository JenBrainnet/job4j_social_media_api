package ru.job4j.socialmedia.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.job4j.socialmedia.model.Account;
import ru.job4j.socialmedia.repository.AccountRepository;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class AccountService {

    private final AccountRepository accountRepository;

    public Account save(Account account) {
        return accountRepository.save(account);
    }

    public boolean update(Account account) {
        if (account.getId() == null || !accountRepository.existsById(account.getId())) {
            return false;
        }
        accountRepository.save(account);
        return true;
    }

    public Optional<Account> findById(Integer id) {
        return accountRepository.findById(id);
    }

    public List<Account> findAll() {
        return accountRepository.findAll();
    }

    public boolean deleteById(Integer id) {
        if (!accountRepository.existsById(id)) {
            return false;
        }
        accountRepository.deleteById(id);
        return true;
    }

}

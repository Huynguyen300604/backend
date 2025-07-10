package com.universitybank.service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.universitybank.entity.Account;
import com.universitybank.entity.User;
import com.universitybank.repository.AccountRepository;

@Service
public class AccountService {
    @Autowired
    private AccountRepository accountRepository;


    public List<Account> getAccountsByUser(User user) {
        return accountRepository.findByUser(user);
    }

    public Optional<Account> getAccountByNumber(String accountNumber) {
        return accountRepository.findByAccountNumber(accountNumber);
    }

    public Account createAccount(User user, String accountNumber) {
        Account account = new Account();
        account.setUser(user);
        account.setAccountNumber(accountNumber);
        account.setBalance(BigDecimal.ZERO);
        account.setStatus("ACTIVE");
        return accountRepository.save(account);
    }

    public void deleteAccount(Account account) {
        accountRepository.delete(account);
    }

    public Account saveAccount(Account account) {
        return accountRepository.save(account);
    }

}

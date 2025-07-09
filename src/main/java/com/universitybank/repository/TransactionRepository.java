package com.universitybank.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.universitybank.entity.Account;
import com.universitybank.entity.Transaction;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    List<Transaction> findByFromAccount(String fromAccount);
    List<Transaction> findByToAccount(String toAccount);
    List<Transaction> findByFromAccount(Account account);
    List<Transaction> findByToAccount(Account account);
}

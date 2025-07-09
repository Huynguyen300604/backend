package com.universitybank.service;

import java.math.BigDecimal;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.universitybank.backend.exception.AccountNotFoundException;
import com.universitybank.backend.exception.InsufficientFundsException;
import com.universitybank.entity.Account;
import com.universitybank.entity.Transaction;
import com.universitybank.repository.AccountRepository;
import com.universitybank.repository.TransactionRepository;

@Service
public class TransactionService {

    private final AccountRepository accountRepository;
    private final TransactionRepository transactionRepository;

    public TransactionService(AccountRepository accRepo, TransactionRepository txRepo) {
        this.accountRepository = accRepo;
        this.transactionRepository = txRepo;
    }

    @Transactional
    public Transaction transfer(
        String fromAccountNumber,
        String toAccountNumber,
        BigDecimal amount,
        String userEmail
    ) {
        Account from = accountRepository.findByAccountNumber(fromAccountNumber)
            .orElseThrow(() -> new AccountNotFoundException(fromAccountNumber));

        if (from.getBalance().compareTo(amount) < 0) {
            throw new InsufficientFundsException(fromAccountNumber);
        }

        Account to = accountRepository.findByAccountNumber(toAccountNumber)
            .orElseThrow(() -> new AccountNotFoundException(toAccountNumber));

        // adjust balances
        from.setBalance(from.getBalance().subtract(amount));
        to.setBalance(to.getBalance().add(amount));

        accountRepository.save(from);
        accountRepository.save(to);

        // ...existing code...
        if (!from.getStatus().equals("ACTIVE") || !to.getStatus().equals("ACTIVE")) {
            throw new RuntimeException("One or both accounts are not active.");
        }
// ...existing code...

        Transaction tx = new Transaction();
        tx.setFromAccount(from.getAccountNumber());
        tx.setToAccount(to.getAccountNumber());
        tx.setAmount(amount);
        tx.setTimestamp(java.time.LocalDateTime.now());
        tx.setStatus("SUCCESS"); // hoặc trạng thái bạn muốn
        tx.setFromAccountObj(from);
        tx.setToAccountObj(to);
        // Nếu có user, set luôn:
        tx.setUser(from.getUser()); // hoặc user phù hợp
        return transactionRepository.save(tx);
        
    }
}
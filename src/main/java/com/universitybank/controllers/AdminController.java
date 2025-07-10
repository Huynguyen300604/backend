package com.universitybank.controllers;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.universitybank.entity.Account;
import com.universitybank.entity.Transaction;
import com.universitybank.entity.User;
import com.universitybank.repository.AccountRepository;
import com.universitybank.repository.TransactionRepository;
import com.universitybank.repository.UserRepository;

@RestController
@RequestMapping("/api/admin")
@PreAuthorize("hasRole('ADMIN')")
public class AdminController {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private TransactionRepository transactionRepository;

    @GetMapping("/users")
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @GetMapping("/accounts")
    public List<Account> getAllAccounts() {
        return accountRepository.findAll();
    }

    @GetMapping("/transactions")
    public List<Transaction> getAllTransactions() {
        return transactionRepository.findAll();
    }

    @DeleteMapping("/user/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable Long id) {
        User user = userRepository.findById(id).orElse(null);
        if (user == null) return ResponseEntity.notFound().build();
        List<Account> accounts = accountRepository.findByUserId(id);
        for (Account acc : accounts) {
            List<Transaction> fromTrans = transactionRepository.findByFromAccount(acc);
            for (Transaction t : fromTrans) {
                t.setFromAccount(null);
                transactionRepository.save(t);
            }
            List<Transaction> toTrans = transactionRepository.findByToAccount(acc);
            for (Transaction t : toTrans) {
                t.setToAccount(null);
                transactionRepository.save(t);
            }
            accountRepository.delete(acc);
        }

        userRepository.delete(user);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/account/{id}")
    public void deleteAccount(@PathVariable Long id) {
        accountRepository.deleteById(id);
    }

    @PostMapping("/user/{id}/disable")
    public String disableUser(@PathVariable Long id) {
        User user = userRepository.findById(id).orElse(null);
        if (user == null) return "Không tìm thấy user";
        if (user.getRole().equals("ROLE_ADMIN")) return "Không thể vô hiệu hóa tài khoản quản trị viên";
        user.setEnabled(false);
        userRepository.save(user);
        return "Đã vô hiệu hóa";
    }

    @PostMapping("/user/{id}/enable")
    public String enableUser(@PathVariable Long id) {
        User user = userRepository.findById(id).orElse(null);
        if (user == null) return "Không tìm thấy user";
        user.setEnabled(true);
        userRepository.save(user);
        return "Đã Cho Phép";
    }
    @PostMapping("/account/{accountNumber}/topup")
    public ResponseEntity<?> topUpAccount(
            @PathVariable String accountNumber,
            @RequestParam("amount") double amount) {
        Account account = accountRepository.findByAccountNumber(accountNumber).orElse(null);
        if (account == null) return ResponseEntity.notFound().build();

        User user = account.getUser();
        if (!user.getRole().equals("ROLE_USER")) {
            return ResponseEntity.badRequest().body("Chỉ các tài khoản người dùng mới có thể được nạp tiền");
        }

        account.setBalance(account.getBalance().add(BigDecimal.valueOf(amount)));
        accountRepository.save(account);
        return ResponseEntity.ok("Nạp tiền vào tài khoản thành công. Số dư mới là: " + account.getBalance());
    }

}
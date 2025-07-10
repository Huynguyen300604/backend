package com.universitybank.controllers;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.universitybank.entity.Account;
import com.universitybank.entity.User;
import com.universitybank.repository.UserRepository;
import com.universitybank.service.AccountService;

@RestController
@RequestMapping("/api/accounts")
public class AccountController {
    @Autowired
    private AccountService accountService;

    @Autowired
    private UserRepository userRepository;

    @GetMapping("/user/{userId}")
    public ResponseEntity<?> getAccountsByUser(@PathVariable Long userId) {
        Optional<User> user = userRepository.findById(userId);
        if (user.isPresent()) {
            List<Account> accounts = accountService.getAccountsByUser(user.get());
            return ResponseEntity.ok(accounts);
        }
        return ResponseEntity.notFound().build();
    }

    @PostMapping("/create")
    public ResponseEntity<?> createAccount(Authentication authentication) {
        String email = authentication.getName();
        User user = userRepository.findByEmail(email).orElse(null);
        if (user == null) {
            return ResponseEntity.badRequest().body("Không tìm thấy user");
        }
        String accountNumber = "CMCB" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
        Account account = accountService.createAccount(user, accountNumber);
        return ResponseEntity.ok(account);
    }

    @DeleteMapping("/{accountNumber}")
    public ResponseEntity<?> deleteAccount(@PathVariable String accountNumber, Authentication authentication) {
        String email = authentication.getName();
        User user = userRepository.findByEmail(email).orElse(null);
        if (user == null) {
            return ResponseEntity.badRequest().body("Không tìm thấy user");
        }
        Optional<Account> account = accountService.getAccountByNumber(accountNumber);
        if (account.isPresent() && account.get().getUser().getId().equals(user.getId())) {
            accountService.deleteAccount(account.get());
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.status(403).body("Forbidden");
    }
}

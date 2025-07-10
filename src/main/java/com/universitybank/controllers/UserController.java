package com.universitybank.controllers;

import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.universitybank.entity.User;
import com.universitybank.service.UserService;

@RestController
@RequestMapping("/api/users")
public class UserController {
    @Autowired
    private UserService userService;

    @GetMapping("/{id}")
    public ResponseEntity<?> getUser(@PathVariable Long id, org.springframework.security.core.Authentication authentication) {
        Optional<User> user = userService.getUserById(id);
        if (user.isPresent()) {
            String email = authentication.getName();
            // Chỉ cho xem thông tin chính mình
            if (!user.get().getEmail().equals(email)) {
                return ResponseEntity.status(403).body("Bạn chỉ có thể xem thông tin tài khoản của chính mình.");
            }
            // Trả về thông tin user kèm message
            return ResponseEntity.ok().body(
                Map.of(
                    "message", "Đây là thông tin tài khoản của bạn.",
                    "user", user.get()
                )
            );
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable Long id, org.springframework.security.core.Authentication authentication) {
        Optional<User> user = userService.getUserById(id);
        if (user.isPresent()) {
            String email = authentication.getName();
            // Chỉ cho xóa chính mình
            if (!user.get().getEmail().equals(email)) {
                return ResponseEntity.status(403).body("Bạn chỉ có thể xóa tài khoản của chính mình.");
            }
            userService.deleteUser(user.get());
            return ResponseEntity.ok().body(
                Map.of("message", "Bạn đã xóa tài khoản của mình thành công.")
            );
        }
        return ResponseEntity.notFound().build();
    }

}

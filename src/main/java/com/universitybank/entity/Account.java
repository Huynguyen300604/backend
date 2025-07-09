package com.universitybank.entity;

import java.math.BigDecimal;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "accounts")
public class Account {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "account_number", nullable = false, unique = true)
    private String accountNumber;

    @Column(nullable = false)
    private BigDecimal balance;

    @Column(nullable = false)
    private String status;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    @JsonIgnore // Ngăn vòng lặp khi trả về JSON
    private User user;

    // One-to-many relationship with transactions
    @OneToMany(mappedBy = "fromAccountObj", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore // Ngăn vòng lặp khi trả về JSON
    private Set<Transaction> outgoingTransactions;

    @OneToMany(mappedBy = "toAccountObj", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore // Ngăn vòng lặp khi trả về JSON
    private Set<Transaction> incomingTransactions;
}
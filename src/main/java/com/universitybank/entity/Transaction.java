package com.universitybank.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonIgnore; // Thêm dòng này

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "transactions")
public class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "amount", nullable = false)
    private BigDecimal amount;

    @Column(name = "timestamp", nullable = false)
    private LocalDateTime timestamp;

    @Column(nullable = false)
    private String status;

    @ManyToOne
    @JoinColumn(name = "from_account", referencedColumnName = "account_number", insertable = false, updatable = false)
    @JsonIgnore // Ngăn vòng lặp khi trả về JSON
    private Account fromAccountObj;

    @Column(name = "from_account", nullable = false)
    private String fromAccount;

    @ManyToOne
    @JoinColumn(name = "to_account", referencedColumnName = "account_number", insertable = false, updatable = false)
    @JsonIgnore // Ngăn vòng lặp khi trả về JSON
    private Account toAccountObj;

    @Column(name = "to_account", nullable = false)
    private String toAccount;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    @JsonIgnore // Ngăn vòng lặp khi trả về JSON
    private User user;
}
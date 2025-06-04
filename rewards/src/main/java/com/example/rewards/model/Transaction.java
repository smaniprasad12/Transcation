package com.example.rewards.model;

import jakarta.persistence.*;


import java.time.LocalDate;

@Entity
@Table(name = "transactions")
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String customerId;

    private LocalDate transactionDate;

    private double amount;


    public Transaction() {}

    public Transaction(String customerId, LocalDate transactionDate, double amount) {
        this.customerId = customerId;
        this.transactionDate = transactionDate;
        this.amount = amount;
    }

    // getters and setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getCustomerId() { return customerId; }
    public void setCustomerId(String customerId) { this.customerId = customerId; }

    public LocalDate getTransactionDate() { return transactionDate; }
    public void setTransactionDate(LocalDate transactionDate) { this.transactionDate = transactionDate; }

    public double getAmount() { return amount; }
    public void setAmount(double amount) { this.amount = amount; }


}


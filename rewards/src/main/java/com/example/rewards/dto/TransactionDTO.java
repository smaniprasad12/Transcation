package com.example.rewards.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.time.LocalDate;


/**
 * Data Transfer Object representing a transaction submitted by a customer.
 * Used when creating new transactions through the API.
 */

public class TransactionDTO {

    @NotBlank(message = "Customer ID is required")
    private String customerId;
    @NotNull(message = "Transaction date is required")
    private LocalDate transactionDate;
    @Positive(message = "Amount must be greater than 0")
    private double amount;


    public TransactionDTO() {}

    public TransactionDTO(String customerId, LocalDate transactionDate, double amount) {
        this.customerId = customerId;
        this.transactionDate = transactionDate;
        this.amount = amount;
    }

    // getters and setters
    public String getCustomerId() { return customerId; }
    public void setCustomerId(String customerId) { this.customerId = customerId; }

    public LocalDate getTransactionDate() { return transactionDate; }
    public void setTransactionDate(LocalDate transactionDate) { this.transactionDate = transactionDate; }

    public double getAmount() { return amount; }
    public void setAmount(double amount) { this.amount = amount; }


}


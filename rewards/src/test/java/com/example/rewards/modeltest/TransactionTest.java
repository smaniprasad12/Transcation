package com.example.rewards.modeltest;



import com.example.rewards.dto.TransactionDTO;
import com.example.rewards.model.Transaction;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

public class TransactionTest {

    @Test
    void testNoArgConstructorAndSetters() {
        // Create DTO for sample data
        TransactionDTO transactionDTO = new TransactionDTO("Customer0", LocalDate.of(2024, 1, 1), 100.0);

        // Use all-args constructor
        Transaction transaction = new Transaction(
                transactionDTO.getCustomerId(),
                transactionDTO.getTransactionDate(),
                transactionDTO.getAmount()
        );

        // Use setters to update fields
        transaction.setId(1L);
        transaction.setCustomerId("Customer1");
        transaction.setTransactionDate(LocalDate.of(2024, 5, 20));
        transaction.setAmount(150.50);

        // Assert getters return expected values
        assertEquals(1L, transaction.getId());
        assertEquals("Customer1", transaction.getCustomerId());
        assertEquals(LocalDate.of(2024, 5, 20), transaction.getTransactionDate());
        assertEquals(150.50, transaction.getAmount());
    }


    @Test
    void testAllArgsConstructor() {
        LocalDate date = LocalDate.of(2024, 5, 20);
        Transaction transaction = new Transaction("Customer1", date, 120.00);

        assertNull(transaction.getId()); // since id is not set manually
        assertEquals("Customer1", transaction.getCustomerId());
        assertEquals(date, transaction.getTransactionDate());
        assertEquals(120.00, transaction.getAmount());
    }
}

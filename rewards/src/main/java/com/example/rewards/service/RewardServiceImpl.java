
package com.example.rewards.service;

import com.example.rewards.model.Transaction;
import com.example.rewards.exception.RewardProcessingException;
import com.example.rewards.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Month;
import java.time.format.TextStyle;
import java.util.*;

@Service
public class RewardServiceImpl implements RewardService {

    @Autowired
    private TransactionRepository transactionRepository;

    /**
     * Calculates reward points for a single transaction amount.
     *
     * @param amount the transaction amount
     * @return the calculated reward points
     */
    @Override
    public int calculatePoints(double amount) {
        if (amount < 0) {
            throw new IllegalArgumentException("Transaction amount cannot be negative: " + amount);
        }

        if (amount <= 50) {
            return 0;
        } else if (amount <= 100) {
            return (int)(amount - 50);
        } else {
            return 50 + (int)((amount - 100) * 2);
        }
    }

    /**
     * Calculates reward points per customer per month.
     *
     * @param transactions the list of transactions to process
     * @return a map where keys are customer IDs, and values are maps of month names to point totals
     */
    @Override
    public Map<String, Map<String, Integer>> calculateMonthlyPoints(List<Transaction> transactions) {
        if (transactions == null) {
            throw new IllegalArgumentException("Transaction list cannot be null");
        }

        Map<String, Map<String, Integer>> customerMonthlyPoints = new HashMap<>();

        for (Transaction t : transactions) {
            if (t == null) continue;

            String customerId = t.getCustomerId();
            if (customerId == null || customerId.isBlank()) {
                throw new RewardProcessingException("Customer ID is missing in transaction.");
            }

            if (t.getTransactionDate() == null) {
                throw new RewardProcessingException("Transaction date is missing for customer ID: " + customerId);
            }

            Month month = t.getTransactionDate().getMonth();
            String monthName = month.getDisplayName(TextStyle.SHORT, Locale.ENGLISH);
            int points = calculatePoints(t.getAmount());

            customerMonthlyPoints
                    .computeIfAbsent(customerId, k -> new HashMap<>())
                    .merge(monthName, points, Integer::sum);
        }

        return customerMonthlyPoints;
    }

    /**
     * Calculates total reward points per customer.
     *
     * @param monthlyPoints a map of customer IDs to their monthly point breakdown
     * @return a map of customer IDs to their total reward points
     */
    @Override
    public Map<String, Integer> calculateTotalPoints(Map<String, Map<String, Integer>> monthlyPoints) {
        if (monthlyPoints == null) {
            throw new IllegalArgumentException("Monthly points map cannot be null");
        }

        Map<String, Integer> totalPoints = new HashMap<>();

        for (Map.Entry<String, Map<String, Integer>> entry : monthlyPoints.entrySet()) {
            String customerId = entry.getKey();
            Map<String, Integer> monthly = entry.getValue();

            if (monthly == null) {
                throw new RewardProcessingException("Monthly data is null for customer ID: " + customerId);
            }

            int sum = monthly.values().stream().mapToInt(Integer::intValue).sum();
            totalPoints.put(customerId, sum);
        }

        return totalPoints;
    }
    /**
     * Retrieves all transactions associated with a specific customer ID.
     *
     * @param customerId the ID of the customer
     * @return a list of {@link Transaction} objects for the specified customer
     * @throws IllegalArgumentException if the customerId is null or empty
     */
    @Override
    public List<Transaction> getTransactionsByCustomerId(String customerId) {
        if (customerId == null || customerId.isBlank()) {
            throw new IllegalArgumentException("Customer ID must not be null or blank");
        }

        return transactionRepository.findByCustomerId(customerId);
    }



}


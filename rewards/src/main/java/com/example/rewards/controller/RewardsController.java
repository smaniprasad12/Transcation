package com.example.rewards.controller;

import com.example.rewards.dto.MonthlyPointsDTO;
import com.example.rewards.dto.TotalPointsDTO;
import com.example.rewards.dto.TransactionDTO;
import com.example.rewards.model.Transaction;
import com.example.rewards.repository.TransactionRepository;
import com.example.rewards.service.RewardService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


/**
 * REST controller that handles endpoints for managing customer transactions
 * and retrieving reward points calculated from those transactions.
 */
@RestController
@RequestMapping("/api/rewards")
public class RewardsController {

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private RewardService rewardService;



    /**
     * Adds a new transaction for a customer.
     * Converts the incoming {@link TransactionDTO} to a {@link Transaction} entity
     * and saves it using the transaction repository.
     *
     * @param transactionDTO the data transfer object containing transaction details
     * @return a success message after saving the transaction
     */

    @PostMapping("/transactions")
    public String addTransaction(@Valid @RequestBody TransactionDTO transactionDTO) {
        // Map DTO to entity
        Transaction transaction = new Transaction(
                transactionDTO.getCustomerId(),
                transactionDTO.getTransactionDate(),

                transactionDTO.getAmount()
        );
        transactionRepository.save(transaction);
        return "Transaction added";
    }

    /**
     * Retrieves reward points earned by each customer, grouped by transaction month.
     *
     * @return a list of {@link MonthlyPointsDTO} containing monthly reward points per customer
     */
    @GetMapping("/points/monthly")
    public List<MonthlyPointsDTO> getMonthlyPoints() {
        List<Transaction> transactions = transactionRepository.findAll();
        Map<String, Map<String, Integer>> monthlyPoints = rewardService.calculateMonthlyPoints(transactions);

        return monthlyPoints.entrySet().stream()
                .map(e -> new MonthlyPointsDTO(e.getKey(), e.getValue()))
                .collect(Collectors.toList());
    }

    /**
     * Retrieves the total reward points earned by each customer across all months.
     *
     * @return a list of {@link TotalPointsDTO} containing total reward points per customer
     */
    @GetMapping("/points/total")
    public List<TotalPointsDTO> getTotalPoints() {
        List<Transaction> transactions = transactionRepository.findAll();
        Map<String, Map<String, Integer>> monthlyPoints = rewardService.calculateMonthlyPoints(transactions);
        Map<String, Integer> totalPoints = rewardService.calculateTotalPoints(monthlyPoints);

        return totalPoints.entrySet().stream()
                .map(e -> new TotalPointsDTO(e.getKey(), e.getValue()))
                .collect(Collectors.toList());
    }

    /**
     * Retrieves all transactions for a specific customer by their customer ID.
     *
     * @PathVariable customerId the ID of the customer whose transactions are to be retrieved
     */
    @GetMapping("/transactions/{customerId}")
    public List<Transaction> getTransactionsByCustomer(@PathVariable String customerId) {
        return rewardService.getTransactionsByCustomerId(customerId);
    }


}


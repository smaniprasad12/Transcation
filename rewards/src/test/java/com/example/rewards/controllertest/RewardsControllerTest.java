package com.example.rewards.controllertest;



import com.example.rewards.controller.RewardsController;

import com.example.rewards.dto.TransactionDTO;
import com.example.rewards.model.Transaction;
import com.example.rewards.repository.TransactionRepository;
import com.example.rewards.service.RewardService;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;

import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;


import java.time.LocalDate;
import java.util.*;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(RewardsController.class)
public class RewardsControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TransactionRepository transactionRepository;

    @MockBean
    private RewardService rewardService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void testAddTransaction() throws Exception {
        TransactionDTO dto = new TransactionDTO("Customer1", LocalDate.of(2024, 5, 10), 120.0);

        mockMvc.perform(post("/api/rewards/transactions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(content().string("Transaction added"));

        Mockito.verify(transactionRepository).save(Mockito.any(Transaction.class));
    }




    @Test
    void testGetMonthlyPoints() throws Exception {
        List<Transaction> mockTransactions = List.of(
                new Transaction("Customer1", LocalDate.of(2024, 5, 10), 120.0)
        );

        Map<String, Map<String, Integer>> mockMonthlyPoints = Map.of(
                "Customer1", Map.of("May", 90)
        );

        when(transactionRepository.findAll()).thenReturn(mockTransactions);
        when(rewardService.calculateMonthlyPoints(mockTransactions)).thenReturn(mockMonthlyPoints);

        mockMvc.perform(get("/api/rewards/points/monthly"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].customerId").value("Customer1"))
                .andExpect(jsonPath("$[0].monthlyPoints.May").value(90));
    }

    @Test
    void testGetTotalPoints() throws Exception {
        List<Transaction> mockTransactions = List.of(
                new Transaction("Customer1", LocalDate.of(2024, 5, 10), 120.0)
        );

        Map<String, Map<String, Integer>> monthlyPoints = Map.of(
                "Customer1", Map.of("May", 90)
        );

        Map<String, Integer> totalPoints = Map.of("Customer1", 90);

        when(transactionRepository.findAll()).thenReturn(mockTransactions);
        when(rewardService.calculateMonthlyPoints(mockTransactions)).thenReturn(monthlyPoints);
        when(rewardService.calculateTotalPoints(monthlyPoints)).thenReturn(totalPoints);

        mockMvc.perform(get("/api/rewards/points/total"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].customerId").value("Customer1"))
                .andExpect(jsonPath("$[0].totalPoints").value(90));
    }


    @Test
    void testGetTransactionsByCustomer() throws Exception {
        String customerId = "Customer1";
        List<Transaction> mockTransactions = List.of(
                new Transaction(customerId, LocalDate.of(2024, 5, 10), 120.0),
                new Transaction(customerId, LocalDate.of(2024, 5, 15), 80.0)
        );

        when(rewardService.getTransactionsByCustomerId(customerId)).thenReturn(mockTransactions);

        mockMvc.perform(get("/api/rewards/transactions/{customerId}", customerId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(mockTransactions.size()))
                .andExpect(jsonPath("$[0].customerId").value(customerId))
                .andExpect(jsonPath("$[0].amount").value(120.0))
                .andExpect(jsonPath("$[1].amount").value(80.0));

        Mockito.verify(rewardService).getTransactionsByCustomerId(customerId);
    }



}


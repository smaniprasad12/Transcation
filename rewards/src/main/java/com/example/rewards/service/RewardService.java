package com.example.rewards.service;

import com.example.rewards.model.Transaction;

import java.util.*;

public interface RewardService {

   public int calculatePoints(double amount);

   public Map<String, Map<String, Integer>> calculateMonthlyPoints(List<Transaction> transactions);

   public Map<String, Integer> calculateTotalPoints(Map<String, Map<String, Integer>> monthlyPoints);

   List<Transaction> getTransactionsByCustomerId(String customerId);

}

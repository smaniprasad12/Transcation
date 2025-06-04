package com.example.rewards.servicetest;



import com.example.rewards.exception.RewardProcessingException;
import com.example.rewards.model.Transaction;
import com.example.rewards.repository.TransactionRepository;
import com.example.rewards.service.RewardServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(MockitoExtension.class)
class RewardServiceImplTest {

    @InjectMocks
    private RewardServiceImpl rewardService;
    @Mock
    private TransactionRepository transactionRepository;

    @BeforeEach
    void setup() {

    }

    @Test
    void testCalculatePoints() {

        assertEquals(0, rewardService.calculatePoints(30));
        assertEquals(10, rewardService.calculatePoints(60));
        assertEquals(50, rewardService.calculatePoints(100));
        assertEquals(90, rewardService.calculatePoints(120));
        assertEquals(100, rewardService.calculatePoints(125));
    }

    @Test
    void testCalculateMonthlyPoints() {
        List<Transaction> transactions = new ArrayList<>();
        transactions.add(new Transaction("cust1", LocalDate.of(2025, 5, 10), 120));
        transactions.add(new Transaction("cust1", LocalDate.of(2025, 5, 20), 75));
        transactions.add(new Transaction("cust1", LocalDate.of(2025, 6, 5), 110));
        transactions.add(new Transaction("cust2", LocalDate.of(2025, 6, 1), 130));

        Map<String, Map<String, Integer>> monthlyPoints = rewardService.calculateMonthlyPoints(transactions);

        assertNotNull(monthlyPoints);
        assertEquals(2, monthlyPoints.size());
        assertEquals(115, monthlyPoints.get("cust1").get("May"));
        assertEquals(70, monthlyPoints.get("cust1").get("Jun"));
        assertEquals(110, monthlyPoints.get("cust2").get("Jun"));
    }

    @Test
    void testCalculateTotalPoints() {
        Map<String, Map<String, Integer>> monthlyPoints = new HashMap<>();

        Map<String, Integer> cust1Points = new HashMap<>();
        cust1Points.put("May", 115);
        cust1Points.put("Jun", 70);

        Map<String, Integer> cust2Points = new HashMap<>();
        cust2Points.put("Jun", 110);

        monthlyPoints.put("cust1", cust1Points);
        monthlyPoints.put("cust2", cust2Points);

        Map<String, Integer> totalPoints = rewardService.calculateTotalPoints(monthlyPoints);

        assertEquals(2, totalPoints.size());
        assertEquals(185, totalPoints.get("cust1"));
        assertEquals(110, totalPoints.get("cust2"));
    }

    @Test
    void testCalculatePoints_NegativeAmount_ShouldThrowException() {
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> {
            rewardService.calculatePoints(-10);
        });
        assertEquals("Transaction amount cannot be negative: -10.0", ex.getMessage());
    }
    @Test
    void testCalculateMonthlyPoints_NullTransactions_ShouldThrowException() {
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> {
            rewardService.calculateMonthlyPoints(null);
        });
        assertEquals("Transaction list cannot be null", ex.getMessage());
    }

    @Test
    void testCalculateMonthlyPoints_TransactionWithNullCustomerId_ShouldThrowRewardProcessingException() {
        List<Transaction> transactions = List.of(
                new Transaction(null, LocalDate.of(2025, 5, 10), 100)
        );

        RewardProcessingException ex = assertThrows(RewardProcessingException.class, () -> {
            rewardService.calculateMonthlyPoints(transactions);
        });
        assertEquals("Customer ID is missing in transaction.", ex.getMessage());
    }
    @Test
    void testCalculateMonthlyPoints_TransactionWithNullDate_ShouldThrowRewardProcessingException() {
        List<Transaction> transactions = List.of(
                new Transaction("cust1", null, 100)
        );

        RewardProcessingException ex = assertThrows(RewardProcessingException.class, () -> {
            rewardService.calculateMonthlyPoints(transactions);
        });
        assertEquals("Transaction date is missing for customer ID: cust1", ex.getMessage());
    }

    @Test
    void testCalculateTotalPoints_NullMonthlyPoints_ShouldThrowException() {
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> {
            rewardService.calculateTotalPoints(null);
        });
        assertEquals("Monthly points map cannot be null", ex.getMessage());
    }

    @Test
    void testGetTransactionsByCustomerId_ValidId() {
        String customerId = "cust1";
        List<Transaction> mockTransactions = Arrays.asList(
                new Transaction(customerId, LocalDate.of(2025, 5, 10), 120),
                new Transaction(customerId, LocalDate.of(2025, 6, 10), 80)
        );

        Mockito.when(transactionRepository.findByCustomerId(customerId)).thenReturn(mockTransactions);

        List<Transaction> result = rewardService.getTransactionsByCustomerId(customerId);

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(customerId, result.get(0).getCustomerId());

        Mockito.verify(transactionRepository, Mockito.times(1)).findByCustomerId(customerId);
    }

    @Test
    void testGetTransactionsByCustomerId_NullCustomerId_ShouldThrowException() {
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> {
            rewardService.getTransactionsByCustomerId(null);
        });
        assertEquals("Customer ID must not be null or blank", ex.getMessage());

        Mockito.verifyNoInteractions(transactionRepository);
    }

    @Test
    void testGetTransactionsByCustomerId_BlankCustomerId_ShouldThrowException() {
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> {
            rewardService.getTransactionsByCustomerId("   ");
        });
        assertEquals("Customer ID must not be null or blank", ex.getMessage());

        Mockito.verifyNoInteractions(transactionRepository);
    }

}


package com.example.rewards.dto;



import java.util.Map;


/**
 * DTO representing the monthly reward points earned by a customer.
 */

public class MonthlyPointsDTO {
    private String customerId;
    private Map<String, Integer> monthlyPoints;

    public MonthlyPointsDTO() {}

    public MonthlyPointsDTO(String customerId, Map<String, Integer> monthlyPoints) {
        this.customerId = customerId;
        this.monthlyPoints = monthlyPoints;
    }

    public String getCustomerId() { return customerId; }
    public void setCustomerId(String customerId) { this.customerId = customerId; }

    public Map<String, Integer> getMonthlyPoints() { return monthlyPoints; }
    public void setMonthlyPoints(Map<String, Integer> monthlyPoints) { this.monthlyPoints = monthlyPoints; }
}

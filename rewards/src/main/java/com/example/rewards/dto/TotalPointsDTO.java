package com.example.rewards.dto;


/**
 * DTO representing the total reward points earned by a customer.
 */

public class TotalPointsDTO {
    private String customerId;
    private int totalPoints;

    public TotalPointsDTO() {}

    public TotalPointsDTO(String customerId, int totalPoints) {
        this.customerId = customerId;
        this.totalPoints = totalPoints;
    }

    public String getCustomerId() { return customerId; }
    public void setCustomerId(String customerId) { this.customerId = customerId; }

    public int getTotalPoints() { return totalPoints; }
    public void setTotalPoints(int totalPoints) { this.totalPoints = totalPoints; }
}



# Rewards Points API

This is a Spring Boot RESTful API for managing customer transactions and calculating reward points based on purchase amounts. 
It allows adding transactions and retrieving both monthly and total reward points for each customer.

---

## Features

- Add a new transaction for a customer
- Calculate reward points:
    - $0–$50: 0 points
    - $50–$100: 1 point per dollar over $50
    - Above $100: 2 points per dollar over $100 (plus 50 points for $50–$100 range)
- Get monthly reward points per customer
- Get total reward points per customer
- Input validation with error handling

---

## Technologies Used

- Java 17+
- Spring Boot
- Spring Web, Spring Data JPA, Hibernate
- my-sql Database 
- JUnit 5, Mockito
- Maven  (build tool)
- Jakarta Validation

---
## API Endpoints

### Add Transaction

```
POST /api/rewards/transactions
```

#### Request Body:
```json
{
  "customerId": "cust123",
  "transactionDate": "2025-06-04",
  "amount": 120.0
}
```

#### Response:
```
Transaction added
```

---

### Get Monthly Points

```
GET /api/rewards/points/monthly
```

#### Response:
```json
[
  {
    "customerId": "cust123",
    "monthlyPoints": {
      "Jun": 90
    }
  }
]
```

---

### 🧮 Get Total Points

```
GET /api/rewards/points/total
```

#### Response:
```json
[
  {
    "customerId": "cust123",
    "totalPoints": 90
  }
]
```

### Get Transactions By Customer ID

```
GET /api/rewards/transactions/{customerId}
```
### Response:
```json
[
 {
   "id": 1,
   "customerId": "CUST1001",
   "transactionDate": "2025-06-01",
   "amount": 120.00
},
{
  "id": 2,
  "customerId": "CUST1001",
  "transactionDate": "2025-05-15",
  "amount": 75.00
 }
]



```

## Validation & Error Handling

- `customerId` must not be blank
- `transactionDate` must not be null
- `amount` must be non-negative

### Example Error Response:
```json
{
  "timestamp": "2025-06-04T12:00:00",
  "status": 400,
  "errors": [
    "Customer ID is required",
    "Transaction date is required"
  ]
}
```

> Make sure to implement `@RestControllerAdvice` for clean global error handling.

---

## Running Tests



Test coverage includes:

- Reward points calculation logic
- Monthly and total aggregation
- Negative test cases (missing data, nulls)
- Controller layer validation

---

## Project Structure

```
com.example.rewards
├── controller
│   └── RewardsController.java
├── dto
├── exception
├── model
│   └── Transaction.java
├── repository
├── service
│   └── RewardServiceImpl.java
├── controllertest
│   └── RewardsControllerTest.java
├── servicetest
│   └── RewardServiceImplTest.java
└── modeltest
    └── ModelTest.java

```

---

## Configuration

In `application.properties`:

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/rewards
spring.datasource.username=****
spring.datasource.password=

spring.jpa.properties.hibernate.dialect =org.hibernate.dialect.MySQL8Dialect
spring.jpa.hibernate.ddl-auto=update
```
## Author

Built with  Siddavtam ManiPrasad

# Order Management System

A comprehensive Spring Boot application demonstrating enterprise-level design patterns and best practices for order management APIs.

## Features

### 1. **REST APIs**
- **Create Order**: `POST /api/v1/orders`
- **Retrieve Order**: `GET /api/v1/orders/{orderId}`
- **Retrieve All Orders**: `GET /api/v1/orders`
- **Update Order Status**: `PUT /api/v1/orders/{orderId}/status`

### 2. **Architecture**
- **MVC Pattern**: Separation of concerns with Controller, Service, and Repository layers
- **H2 Database**: In-memory relational database with automatic schema generation via Hibernate
- **Spring Data JPA**: Repository abstraction for database operations
- **Global Exception Handling**: Centralized error handling with custom exceptions

### 3. **Design Patterns Implemented**

#### Strategy Pattern
Multiple notification strategies for sending alerts:
- Email Notification
- SMS Notification

#### Factory Pattern
`NotificationStrategyFactory` for runtime selection of notification strategies based on type.

#### Template Method Pattern
`OrderProcessingTemplate` defines the standardized flow for order processing:
1. Validate Order
2. Update Order Status
3. Notify Customer
4. Log Processing

#### Decorator Pattern
- `LoggingNotificationDecorator`: Adds logging behavior to notifications
- `RetryableNotificationDecorator`: Adds retry logic with configurable attempts

#### Observer Pattern
`OrderStatusManager` and `OrderStatusObserver` for reacting to order status changes:
- `OrderStatusChangeListener` listens for status changes and handles shipping/completion/cancellation

### 4. **Order Status Management**
- Valid initial status: `CREATED`
- Valid statuses: `CREATED`, `IN_PROGRESS`, `COMPLETED`, `CANCELLED`
- Valid transitions: `CREATED → IN_PROGRESS`, `IN_PROGRESS → COMPLETED`, `IN_PROGRESS → CANCELLED`, `CREATED → CANCELLED`
- Invalid transitions are rejected with appropriate HTTP status codes

### 5. **Request Validation**
- Email format validation
- Required field validation
- Amount validation (must be positive)
- Returns `400 Bad Request` for validation errors

### 6. **Testing**
- Unit tests for service layer using Mockito
- Tests cover:
  - Successful order creation
  - Order retrieval (found and not found scenarios)
  - Valid status transitions
  - Invalid status transitions
  - Validation logic

## Project Structure

```
src/
├── main/java/com/order/management/
│   ├── OrderManagementApplication.java       # Spring Boot entry point
│   ├── controller/
│   │   └── OrderController.java              # REST endpoints
│   ├── service/
│   │   ├── OrderService.java                 # Business logic & orchestration
│   │   └── OrderProcessingTemplate.java      # Template Method pattern
│   ├── repository/
│   │   └── OrderRepository.java              # JPA Repository (H2 persistence)
│   ├── model/
│   │   ├── Order.java                        # JPA Entity with H2 annotations
│   │   └── OrderStatus.java                  # Order status enum
│   ├── dto/
│   │   ├── CreateOrderRequest.java           # Create order request DTO
│   │   ├── UpdateOrderStatusRequest.java     # Update status request DTO
│   │   └── OrderResponse.java                # Order response DTO
│   ├── exception/
│   │   ├── GlobalExceptionHandler.java       # Global exception handler
│   │   ├── OrderNotFoundException.java       # Custom exception
│   │   ├── InvalidOrderStatusTransitionException.java
│   │   └── ErrorResponse.java                # Error response DTO
│   ├── notification/
│   │   ├── NotificationStrategy.java         # Strategy interface
│   │   ├── EmailNotificationStrategy.java    # Email implementation
│   │   ├── SMSNotificationStrategy.java      # SMS implementation
│   │   ├── NotificationStrategyFactory.java  # Factory pattern
│   │   ├── LoggingNotificationDecorator.java # Decorator with logging
│   │   └── RetryableNotificationDecorator.java # Decorator with retry
│   └── observer/
│       ├── OrderStatusObserver.java          # Observer interface
│       ├── OrderStatusManager.java           # Observer manager
│       ├── OrderStatusChangeListener.java    # Listener implementation
│       └── EmailNotificationObserver.java    # Email notification observer
├── resources/
│   └── application.properties                # Application configuration with H2 settings
└── test/java/com/order/management/
    └── service/
        └── OrderServiceTest.java             # Unit tests
```

## Prerequisites

- Java 17 or higher
- Maven 3.6.0 or higher

## Setup Instructions

### 1. Build the Project
```bash
mvn clean install
```

### 2. Run Tests
```bash
mvn test
```

### 3. Run the Application
```bash
mvn spring-boot:run
```

The application will start on `http://localhost:8080`

### 4. Access H2 Database Console (Optional)
```
URL: http://localhost:8080/h2-console
JDBC URL: jdbc:h2:mem:orderdb
Username: sa
Password: (leave empty)
```

Once connected, you can query the database:
```sql
-- View all orders
SELECT * FROM ORDERS;

-- View specific order
SELECT * FROM ORDERS WHERE ID = 'your-order-id';

-- Count total orders
SELECT COUNT(*) FROM ORDERS;
```

## API Examples

### Create Order
```bash
curl -X POST http://localhost:8080/api/v1/orders \
  -H "Content-Type: application/json" \
  -d '{
    "customerName": "John Doe",
    "customerEmail": "john@example.com",
    "mobileNumber": "9876543210",
    "totalAmount": 150.00
  }'
```

**Response** (201 Created):
```json
{
  "id": "550e8400-e29b-41d4-a716-446655440000",
  "customerName": "John Doe",
  "customerEmail": "john@example.com",
  "mobileNumber": "9876543210",
  "totalAmount": 150.00,
  "status": "CREATED",
  "createdAt": "2024-01-28T10:30:00",
  "updatedAt": "2024-01-28T10:30:00"
}
```

### Get Order
```bash
curl http://localhost:8080/api/v1/orders/550e8400-e29b-41d4-a716-446655440000
```

### Get All Orders
```bash
curl http://localhost:8080/api/v1/orders
```

### Update Order Status
```bash
curl -X PUT http://localhost:8080/api/v1/orders/550e8400-e29b-41d4-a716-446655440000/status \
  -H "Content-Type: application/json" \
  -d '{"status": "COMPLETED"}'
```

## Error Handling

The application provides consistent error responses:

```json
{
  "status": 404,
  "message": "Order not found with ID: invalid-id",
  "error": "Order Not Found",
  "timestamp": "2024-01-28T10:35:00"
}
```

### HTTP Status Codes
- `200 OK`: Successful GET/PUT
- `201 Created`: Successful POST
- `400 Bad Request`: Validation error or invalid status transition
- `404 Not Found`: Order not found
- `500 Internal Server Error`: Unexpected server error

## Design Pattern Details

### Strategy Pattern
Different notification channels can be selected at runtime:
```java
NotificationStrategy emailStrategy = factory.getStrategy("EMAIL");
NotificationStrategy smsStrategy = factory.getStrategy("SMS");
```

### Template Method Pattern
`OrderProcessingTemplate` defines a fixed workflow that subclasses customize.

## Logging

Application logs are written to the console and include:
- DEBUG level logs for package `com.order.management`
- INFO level logs for Spring framework and other dependencies

Configure logging in `application.properties`:
```properties
logging.level.com.order.management=DEBUG
logging.level.root=INFO
```

## Dependencies

- Spring Boot 3.1.5
- Spring Web
- Spring Data JPA
- Spring Validation
- H2 Database
- Lombok
- JUnit 5
- Mockito


## Author

Order Management System - Demonstrates enterprise design patterns in Spring Boot

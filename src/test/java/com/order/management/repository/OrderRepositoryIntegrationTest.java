package com.order.management.repository;

import com.order.management.model.NotificationType;
import com.order.management.model.Order;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
public class OrderRepositoryIntegrationTest {

    @Autowired
    private OrderRepository orderRepository;

    @Test
    public void testSaveAndFind_OrderPersistedSuccessfully() {
        // Arrange
        Order order = new Order("John Doe", "john@example.com", "9876543210", new BigDecimal("100.00"), NotificationType.EMAIL);

        // Act
        Order saved = orderRepository.save(order);

        // Assert
        assertNotNull(saved.getId());
        Optional<Order> loaded = orderRepository.findById(saved.getId());
        assertTrue(loaded.isPresent());
        assertEquals("John Doe", loaded.get().getCustomerName());
        assertEquals(new BigDecimal("100.00"), loaded.get().getTotalAmount());
    }
    @Test
    public void testUpdateOrder_PersistedChanges() {
        Order order = new Order("Alice", "alice@example.com", "0123456789", new BigDecimal("50.00"), NotificationType.SMS);
        Order saved = orderRepository.save(order);

        // modify and save
        saved.setCustomerName("Alice Smith");
        saved.setTotalAmount(new BigDecimal("75.00"));
        orderRepository.save(saved);

        Optional<Order> updated = orderRepository.findById(saved.getId());
        assertTrue(updated.isPresent());
        assertEquals("Alice Smith", updated.get().getCustomerName());
        assertEquals(new BigDecimal("75.00"), updated.get().getTotalAmount());
    }

    @Test
    public void testFindById_ReturnsEmptyForUnknownId() {
        Optional<Order> result = orderRepository.findById("non-existing-id");
        assertFalse(result.isPresent());
    }
}

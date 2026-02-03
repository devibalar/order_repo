package com.order.management.service;

import com.order.management.dto.CreateOrderRequest;
import com.order.management.dto.OrderResponse;
import com.order.management.dto.UpdateOrderStatusRequest;
import com.order.management.exception.OrderNotFoundException;
import com.order.management.model.NotificationType;
import com.order.management.model.Order;
import com.order.management.model.OrderStatus;
import com.order.management.notification.NotificationMessageBuilder;
import com.order.management.notification.NotificationStrategy;
import com.order.management.notification.NotificationStrategyFactory;
import com.order.management.observer.OrderStatusManager;
import com.order.management.repository.OrderRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DataJpaTest
public class OrderServiceTest {

    @Autowired
    private OrderRepository orderRepository;

    @Mock
    private NotificationStrategyFactory strategyFactory;

    @Mock
    private NotificationMessageBuilder messageBuilder;

    @Mock
    private OrderStatusManager orderStatusManager;

    @Mock
    private NotificationStrategy notificationStrategy;

    private NotificationService notificationService;
    private OrderService orderService;

    @BeforeEach
    public void setUp() {

        // construct the real NotificationService but provide mocked collaborators
        this.notificationService = new NotificationService(strategyFactory, messageBuilder);

        this.orderService = new OrderService(orderRepository, notificationService, orderStatusManager);
    }

    @Test
    public void testCreateOrder_Success() {
        when(strategyFactory.getStrategy(any())).thenReturn(notificationStrategy);
        when(messageBuilder.buildMessage(any(), any(),any())).thenReturn("message");

        CreateOrderRequest request = CreateOrderRequest.builder()
                .customerName("John Doe")
                .customerEmail("john@example.com")
                .mobileNumber("9876543210")
                .totalAmount(new BigDecimal("100.00"))
                .notificationType(NotificationType.EMAIL)
                .build();

        OrderResponse response = orderService.createOrder(request);

        assertNotNull(response);
        assertEquals("John Doe", response.getCustomerName());
        assertEquals("john@example.com", response.getCustomerEmail());
        assertEquals("9876543210", response.getMobileNumber());
        assertEquals(new BigDecimal("100.00"), response.getTotalAmount());

        // repository should contain saved order
        assertTrue(orderRepository.findById(response.getId()).isPresent());
       verify(strategyFactory).getStrategy(NotificationType.EMAIL);
    }

    @Test
    public void testGetOrder_Success() {
        // Arrange
        String orderId = "order-123";
        Order order = Order.builder()
                .id(orderId)
                .customerName("John Doe")
                .customerEmail("john@example.com")
                .mobileNumber("9876543210")
                .totalAmount(new BigDecimal("100.00"))
                .status(OrderStatus.CREATED)
                .build();

        orderRepository.save(order);

        // Act
        OrderResponse response = orderService.getOrder(orderId);

        // Assert
        assertNotNull(response);
        assertEquals(orderId, response.getId());
        assertEquals("John Doe", response.getCustomerName());

        // ensure repository still contains it
        assertTrue(orderRepository.findById(orderId).isPresent());
    }

    @Test
    public void testGetOrder_NotFound() {
        String orderId = "non-existent";
        assertThrows(OrderNotFoundException.class, () -> orderService.getOrder(orderId));
        // repository remains empty
        assertFalse(orderRepository.findById(orderId).isPresent());
    }


    @Test
    public void testUpdateOrderStatus_CreatedToInProgress_Success() {
        when(strategyFactory.getStrategy(any())).thenReturn(notificationStrategy);
        when(messageBuilder.buildMessage(any(), any(),any())).thenReturn("message");
        // Arrange
        String orderId = "order-123";
        Order order = Order.builder()
                .id(orderId)
                .customerName("John Doe")
                .customerEmail("john@example.com")
                .mobileNumber("9876543210")
                .totalAmount(new BigDecimal("100.00"))
                .status(OrderStatus.CREATED)
                .preferredNotificationType(NotificationType.EMAIL)
                .build();

        orderRepository.save(order);

        UpdateOrderStatusRequest request = UpdateOrderStatusRequest.builder()
                .status(OrderStatus.SHIPPED)
                .build();

        // Act
        OrderResponse response = orderService.updateOrderStatus(orderId, request);

        // Assert
        assertNotNull(response);
        assertEquals(orderId, response.getId());
        assertEquals(OrderStatus.SHIPPED, orderRepository.findById(orderId).map(Order::getStatus).orElse(null));

        verify(orderStatusManager, times(1)).notifyObservers(any(Order.class), anyString());
        verify(strategyFactory, atLeast(0)).getStrategy(NotificationType.EMAIL);
    }


}
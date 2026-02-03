package com.order.management.service;

import com.order.management.model.NotificationType;
import com.order.management.model.Order;
import com.order.management.model.OrderStatus;
import com.order.management.notification.NotificationMessageBuilder;
import com.order.management.notification.NotificationStrategy;
import com.order.management.notification.NotificationStrategyFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class NotificationServiceTest {

    private NotificationStrategyFactory notificationFactory;
    private NotificationMessageBuilder messageBuilder;
    private NotificationStrategy strategy;
    private NotificationService notificationService;

    @BeforeEach
    void setUp() {
        notificationFactory = mock(NotificationStrategyFactory.class);
        messageBuilder = mock(NotificationMessageBuilder.class);
        strategy = mock(NotificationStrategy.class);

        notificationService = new NotificationService(notificationFactory, messageBuilder);
    }

    @Test
    void testSendOrderNotification_Email() {
        // Arrange
        Order order = Order.builder()
                .customerEmail("john@example.com")
                .mobileNumber("12345")
                .status(OrderStatus.CREATED)
                .preferredNotificationType(NotificationType.EMAIL)
                .build();

        when(notificationFactory.getStrategy(NotificationType.EMAIL))
                .thenReturn(strategy);

        when(messageBuilder.buildMessage(order, OrderStatus.CREATED, NotificationType.EMAIL))
                .thenReturn("Email notification: Your order has been created.");

        notificationService.sendOrderNotification(order, NotificationType.EMAIL, OrderStatus.CREATED);

        // Assert
        verify(notificationFactory).getStrategy(NotificationType.EMAIL);
        verify(messageBuilder).buildMessage(order, OrderStatus.CREATED, NotificationType.EMAIL);

    }

    @Test
    void testSendOrderNotification_SMS() {
        // Arrange
        Order order = Order.builder()
                .customerEmail("alice@example.com")
                .mobileNumber("98765")
                .status(OrderStatus.SHIPPED)
                .preferredNotificationType(NotificationType.SMS)
                .build();

        when(notificationFactory.getStrategy(NotificationType.SMS))
                .thenReturn(strategy);

        when(messageBuilder.buildMessage(order, OrderStatus.SHIPPED, NotificationType.SMS))
                .thenReturn("SMS notification: Your order has been shipped.");

        // Act
        notificationService.sendOrderNotification(order, NotificationType.SMS, OrderStatus.SHIPPED);

        // Assert
        verify(notificationFactory).getStrategy(NotificationType.SMS);
        verify(messageBuilder).buildMessage(order, OrderStatus.SHIPPED, NotificationType.SMS);

    }

    @Test
    void testSendOrderNotification_ThrowsIfStrategyMissing() {
        // Arrange
       /* Order order = Order.builder().id.build();

        when(notificationFactory.getStrategy(NotificationType.EMAIL))
                .thenThrow(new IllegalArgumentException("Unsupported notification type"));

        // Act + Assert
        assertThrows(IllegalArgumentException.class, () ->
                notificationService.sendOrderNotification(order, NotificationType.EMAIL, OrderStatus.CREATED)
        );*/
    }
}

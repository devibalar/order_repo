package com.order.management.notification;

import com.order.management.model.NotificationType;
import com.order.management.model.Order;
import com.order.management.model.OrderStatus;
import org.springframework.stereotype.Component;

@Component
public class NotificationMessageBuilder {

    public String buildMessage(Order order, OrderStatus status, NotificationType type) {

        String base = switch (status) {
            case CREATED -> "Your order has been created.";
            case SHIPPED -> "Your order has been shipped.";
            case COMPLETED -> "Your order has been delivered.";
            case CANCELLED -> "Your order has been cancelled.";
        };

        String channel = (type == NotificationType.EMAIL)
                ? "Email notification: "
                : "SMS notification: ";

        return channel + base + " Order ID: " + order.getId();
    }
}

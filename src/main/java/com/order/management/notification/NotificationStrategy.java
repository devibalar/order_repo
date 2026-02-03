package com.order.management.notification;

import com.order.management.model.NotificationType;
import com.order.management.model.Order;

public interface NotificationStrategy {
    void send(Order order, String message);
    NotificationType getType();
}

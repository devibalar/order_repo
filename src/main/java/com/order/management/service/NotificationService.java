package com.order.management.service;

import com.order.management.model.NotificationType;
import com.order.management.model.Order;
import com.order.management.model.OrderStatus;
import com.order.management.notification.NotificationMessageBuilder;
import com.order.management.notification.NotificationStrategy;
import com.order.management.notification.NotificationStrategyFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class NotificationService {

    private final NotificationStrategyFactory notificationFactory;
    private final NotificationMessageBuilder messageBuilder;


    public NotificationService(NotificationStrategyFactory notificationFactory,
                               NotificationMessageBuilder messageBuilder) {
        this.notificationFactory = notificationFactory;
        this.messageBuilder = messageBuilder;
    }

    @Async("notificationExecutor")
    public void sendOrderNotification(Order order, NotificationType type, OrderStatus status) {

        String message = messageBuilder.buildMessage(order, status, type);

        NotificationStrategy strategy = notificationFactory.getStrategy(type);

        strategy.send(order, message);
    }


}

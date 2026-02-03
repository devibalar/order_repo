package com.order.management.notification;

import com.order.management.model.Order;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class LoggingNotificationDecorator extends NotificationStrategyDecorator {

    protected LoggingNotificationDecorator(NotificationStrategy delegate) {
        super(delegate);
    }

    @Override
    public void send(Order order, String message) {
        log.info("=== Starting Notification Process ===");
        log.info("Notification Strategy: {}", super.getClass().getSimpleName());
        log.info("Order ID: {}", order.getId());
        log.info("Customer: {}", order.getCustomerName());
        log.info("Message: {}", message);

        long startTime = System.currentTimeMillis();
        try {
            super.send(order, message);
            long endTime = System.currentTimeMillis();
            log.info("Notification completed successfully in {} ms", endTime - startTime);
        } catch (Exception e) {
            log.error("Notification failed with error: {}", e.getMessage(), e);
            throw e;
        }
        log.info("=== Notification Process Completed ===");
    }

}

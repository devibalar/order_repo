package com.order.management.notification;

import com.order.management.model.Order;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class RetryNotificationDecorator extends NotificationStrategyDecorator {
    private final int maxRetries;

    public RetryNotificationDecorator(NotificationStrategy delegate, int maxRetries) {
        super(delegate);
        this.maxRetries = maxRetries;
    }

    @Override
    public void send(Order order, String message) {
        int attempt = 0;
        while (attempt < maxRetries) {
            try {
                super.send(order, message);
                log.info("Notification sent successfully on attempt {}", attempt + 1);
                return;
            } catch (Exception e) {
                attempt++;
                log.warn("Notification failed on attempt {}. Retrying...", attempt, e);
                if (attempt >= maxRetries) {
                    log.error("Notification failed after {} attempts", maxRetries);
                    throw new RuntimeException("Failed to send notification after " + maxRetries + " attempts", e);
                }
            }
        }
    }

}

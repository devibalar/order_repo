package com.order.management.notification;

import com.order.management.model.NotificationType;
import com.order.management.model.Order;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class EmailNotificationStrategy implements NotificationStrategy {
    /**
     * Send email to customer - TODO: replace mock calls
     * @param order
     * @param message
     */
    @Override
    public void send(Order order, String message) {
        log.info("Sending email notification to {} for order {}", order.getCustomerEmail(), order.getId());
        log.info("Message: {}", message);
        // Mock email service call
        mockEmailServiceCall(order.getCustomerEmail(), message);
    }

    @Override
    public NotificationType getType() {
        return NotificationType.EMAIL;
    }

    private void mockEmailServiceCall(String email, String message) {
        log.info("Mock Email Service: Email sent to {} with content: {}", email, message);
    }
}

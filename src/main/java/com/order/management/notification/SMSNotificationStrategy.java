package com.order.management.notification;

import com.order.management.model.NotificationType;
import com.order.management.model.Order;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class SMSNotificationStrategy implements NotificationStrategy {
    /**
     * send email to customer
     * @param order
     * @param message
     */
    @Override
    public void send(Order order, String message) {
        log.info("Sending SMS notification to {} for order {}",order.getMobileNumber(), order.getId());
        log.info("Message: {}", message);
        // Mock SMS service call
        mockSMSServiceCall(order.getId(), message);
    }

    @Override
    public NotificationType getType() {
        return NotificationType.SMS;
    }

    private void mockSMSServiceCall(String orderId, String message) {
        log.info("Mock SMS Service: SMS sent for order {} with content: {}", orderId, message);
    }
}

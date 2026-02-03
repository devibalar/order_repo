package com.order.management.notification;

import com.order.management.model.NotificationType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class NotificationStrategyFactory {

    private final Map<NotificationType, NotificationStrategy> strategiesMap = new HashMap<>();;

    @Autowired
    public NotificationStrategyFactory(List<NotificationStrategy> services) {
        for (NotificationStrategy service : services) {
            strategiesMap.put(service.getType(), service);
        }
    }
    public NotificationStrategy getStrategy(NotificationType type) {
        NotificationStrategy strategy = strategiesMap.get(type);
        if (strategy == null) {
            throw new IllegalArgumentException("No strategy found for channel: " + type);
        }
        // Apply decorators
        strategy = new LoggingNotificationDecorator(strategy);
        strategy = new RetryNotificationDecorator(strategy, 3);

        return strategy;
    }
}

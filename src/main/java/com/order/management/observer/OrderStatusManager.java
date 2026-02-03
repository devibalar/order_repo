package com.order.management.observer;

import com.order.management.model.Order;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.*;

@Slf4j
@Component
public class OrderStatusManager {

    private final List<OrderStatusObserver> observers;

    public OrderStatusManager(List<OrderStatusObserver> observers) {
        this.observers = observers; // Spring injects ALL observers automatically
        observers.forEach(o ->
                log.info("Observer auto-registered: {}", o.getClass().getSimpleName())
        );
    }

    public void notifyObservers(Order order, String previousStatus) {
        for (OrderStatusObserver observer : observers) {
            observer.onOrderStatusChange(order, previousStatus);
        }
    }
}

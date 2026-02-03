package com.order.management.observer;

import com.order.management.exception.InvalidOrderStatusTransitionException;
import com.order.management.model.Order;
import com.order.management.model.OrderStatus;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class OrderStatusChangeListener implements OrderStatusObserver {

    /**
     * When the order status is changed, payment processing will be initiated here
     * @param order
     * @param previousStatus
     */
    @Override
    public void onOrderStatusChange(Order order, String previousStatus) {
        log.info("Order Status Changed - Order ID: {}, Previous Status: {}, Current Status: {}", 
                order.getId(), previousStatus, order.getStatus());

        // Perform actions based on status change
        switch (order.getStatus()) {
            case CREATED:
                log.info("Order created: {}", order.getId());
                handleOrderInProgress(order);
                break;
            case SHIPPED:
                log.info("Order in progress: {}", order.getId());
                handleOrderInProgress(order);
                break;
            case COMPLETED:
                log.info("Order completed: {}", order.getId());
                handleOrderCompletion(order);
                break;
            case CANCELLED:
                log.info("Order cancelled: {}", order.getId());
                handleOrderCancellation(order);
                break;
            default:
                log.info("Order status: {}", order.getStatus());
        }
    }

    private void handleOrderInProgress(Order order) {
        log.info("Processing in-progress for order: {}", order.getId());
        // Additional logic for order in-progress
    }

    private void handleOrderCompletion(Order order) {
        log.info("Processing completion for order: {}", order.getId());
        // Additional logic for order completion
    }

    private void handleOrderCancellation(Order order) {
        log.info("Processing cancellation for order: {}", order.getId());

    }

}

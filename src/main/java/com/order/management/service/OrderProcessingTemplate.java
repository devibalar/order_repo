package com.order.management.service;

import com.order.management.model.Order;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public abstract class OrderProcessingTemplate {

    public final void processOrder(Order order) {
        log.info("Starting order processing for order: {}", order.getId());

        validateOrder(order);
        notifyCustomer(order);
        logOrderProcessing(order);

        log.info("Order processing completed for order: {}", order.getId());
    }

    protected abstract void validateOrder(Order order);

    protected abstract void notifyCustomer(Order order);

    private void logOrderProcessing(Order order) {
        log.info("Order {} processed. Status: {}, Amount: {}", 
                order.getId(), order.getStatus(), order.getTotalAmount());
    }
}

package com.order.management.observer;

import com.order.management.model.Order;

public interface OrderStatusObserver {
    void onOrderStatusChange(Order order, String previousStatus);
}

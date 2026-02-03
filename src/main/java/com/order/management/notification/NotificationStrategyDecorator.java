package com.order.management.notification;

import com.order.management.model.NotificationType;
import com.order.management.model.Order;

public abstract class NotificationStrategyDecorator implements NotificationStrategy {

    protected final NotificationStrategy delegate;

    protected NotificationStrategyDecorator(NotificationStrategy delegate) {
        this.delegate = delegate;
    }

    @Override
    public void send(Order order, String message) {
        delegate.send(order, message);
    }

    @Override
    public NotificationType getType(){
        return delegate.getType();
    }
}

package com.order.management;

import com.order.management.observer.OrderStatusChangeListener;
import com.order.management.observer.OrderStatusManager;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

@SpringBootApplication
public class OrderManagementApplication {

    public static void main(String[] args) {
        ApplicationContext context = SpringApplication.run(OrderManagementApplication.class, args);

    }
}

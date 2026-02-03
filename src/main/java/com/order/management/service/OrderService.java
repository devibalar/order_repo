package com.order.management.service;

import com.order.management.dto.CreateOrderRequest;
import com.order.management.dto.OrderResponse;
import com.order.management.dto.UpdateOrderStatusRequest;
import com.order.management.exception.InvalidOrderStatusTransitionException;
import com.order.management.exception.OrderNotFoundException;
import com.order.management.model.Order;
import com.order.management.model.OrderStatus;
import com.order.management.observer.OrderStatusManager;
import com.order.management.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderService extends OrderProcessingTemplate {
    private final OrderRepository orderRepository;
    private final NotificationService notificationService;
    private final OrderStatusManager orderStatusManager;

    private Order currentOrder;
    private String previousStatus;
    private static final Logger log = LoggerFactory.getLogger(OrderProcessingTemplate.class);

    /**
     * Creates order in db, validate order
     * @param request
     * @return
     */
    public OrderResponse createOrder(CreateOrderRequest request) {
        log.info("Creating new order for customer: {}", request.getCustomerName());

        Order order = new Order(request.getCustomerName(), 
                               request.getCustomerEmail(), 
                               request.getMobileNumber(),
                               request.getTotalAmount(),
                               request.getNotificationType());

        Order savedOrder = orderRepository.save(order);

        // Use template method for order processing
        this.currentOrder = savedOrder;
        processOrder(savedOrder);

        log.info("Order created successfully with ID: {}", savedOrder.getId());
        return mapToResponse(savedOrder);
    }

    public OrderResponse getOrder(String orderId) {
        log.info("Fetching order: {}", orderId);
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new OrderNotFoundException("Order not found with ID: " + orderId));
        return mapToResponse(order);
    }

    public List<OrderResponse> getAllOrders() {
        log.info("Fetching all orders");
        return orderRepository.findAll().stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    public OrderResponse updateOrderStatus(String orderId, UpdateOrderStatusRequest request) {
        log.info("Updating order status for order: {} to {}", orderId, request.getStatus());

        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new OrderNotFoundException("Order not found with ID: " + orderId));

        validateStatusTransition(order.getStatus(), request.getStatus());
        
        this.previousStatus = order.getStatus().toString();
        order.setStatus(request.getStatus());
        order.setUpdatedAt(java.time.LocalDateTime.now());

        Order updatedOrder = orderRepository.save(order);

        // Use template method for order processing
        this.currentOrder = updatedOrder;
        processOrder(updatedOrder);

        // Notify observers about status change
        orderStatusManager.notifyObservers(updatedOrder, previousStatus);

        log.info("Order status updated successfully");
        return mapToResponse(updatedOrder);
    }

    private void validateStatusTransition(OrderStatus currentStatus, OrderStatus newStatus) {
        if (currentStatus == OrderStatus.CREATED) {
            if (newStatus != OrderStatus.SHIPPED && newStatus != OrderStatus.COMPLETED && newStatus != OrderStatus.CANCELLED) {
                throw new InvalidOrderStatusTransitionException(
                        "Invalid transition from " + currentStatus + " to " + newStatus);
            }
        } else if (currentStatus == OrderStatus.SHIPPED) {
            if (newStatus != OrderStatus.COMPLETED && newStatus != OrderStatus.CANCELLED) {
                throw new InvalidOrderStatusTransitionException(
                        "Invalid transition from " + currentStatus + " to " + newStatus);
            }
        } else if (currentStatus == OrderStatus.COMPLETED || currentStatus == OrderStatus.CANCELLED) {
            throw new InvalidOrderStatusTransitionException(
                    "Cannot transition from terminal status " + currentStatus);
        }
    }

    @Override
    protected void validateOrder(Order order) {
        log.info("Validating order: {}", order.getId());
        if (order.getTotalAmount().signum() <= 0) {
            throw new IllegalArgumentException("Order amount must be positive");
        }
    }

    @Override
    protected void notifyCustomer(Order order) {
        log.info("Notifying customer for order: {}", order.getId());

        notificationService.sendOrderNotification(
                order,
                order.getPreferredNotificationType(),
                order.getStatus()
        );


    }

    private OrderResponse mapToResponse(Order order) {
        return OrderResponse.builder()
                .id(order.getId())
                .customerName(order.getCustomerName())
                .customerEmail(order.getCustomerEmail())
                .mobileNumber(order.getMobileNumber())
                .totalAmount(order.getTotalAmount())
                .status(order.getStatus())
                .createdAt(order.getCreatedAt())
                .updatedAt(order.getUpdatedAt())
                .build();
    }
}

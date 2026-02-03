package com.order.management.repository;

import com.order.management.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepository extends JpaRepository<Order, String> {
    // JpaRepository provides save(), findById(), findAll(), delete(), etc.
}

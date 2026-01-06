package com.fleetops.gateway.repository;

import com.fleetops.gateway.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long> {
}
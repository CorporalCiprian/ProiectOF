package com.fleetops.gateway.model;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;
import java.util.List;
import java.util.ArrayList;

@Entity
@Table(name = "orders")
@Data
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long vehicleId;
    private double startX;
    private double startY;
    private double endX;
    private double endY;
    private String status;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<RoutePoint> route = new ArrayList<>();

    private LocalDateTime createdAt = LocalDateTime.now();
}
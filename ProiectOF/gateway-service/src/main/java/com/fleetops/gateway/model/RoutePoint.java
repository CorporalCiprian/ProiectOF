package com.fleetops.gateway.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Entity
@Table(name = "route_points")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RoutePoint {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private double x;
    private double y;

    @ManyToOne
    @JoinColumn(name = "order_id")
    private Order order;
}
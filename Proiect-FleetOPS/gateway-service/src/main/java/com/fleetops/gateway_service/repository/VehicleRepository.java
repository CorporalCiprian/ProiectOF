package com.fleetops.gateway_service.repository;

import com.fleetops.gateway_service.model.Vehicle;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VehicleRepository extends JpaRepository<Vehicle, Long> {
}
package com.fleetops.gateway.repository;

import com.fleetops.gateway.model.Vehicle;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VehicleRepository extends JpaRepository<Vehicle, Long> {
}
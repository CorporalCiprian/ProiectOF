package com.fleetops.gateway.controller;

import com.fleetops.gateway.service.MovementSimulator;
import com.fleetops.gateway.model.Vehicle;
import com.fleetops.gateway.repository.VehicleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.fleetops.gateway.service.RoutingService;

import java.util.Map;
import java.util.List;

@RestController
@RequestMapping("/api/vehicles")
public class VehicleController {

    @Autowired
    private VehicleRepository vehicleRepository;

    @GetMapping
    public List<Vehicle> getAllVehicles() {
        return vehicleRepository.findAll();
    }

    @Autowired
    private RoutingService routingService;

    @GetMapping("/{id}/calculate-path")
    public Object calculatePath(@PathVariable Long id, @RequestParam double destX, @RequestParam double destY) {
        Vehicle v = vehicleRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Camionul nu există în baza de date!"));

        return routingService.getRouteFromCpp(v.getCurrentX(), v.getCurrentY(), destX, destY);
    }

    @Autowired
    private MovementSimulator movementSimulator;

    @PostMapping("/{id}/start-trip")
    public String startTrip(@PathVariable Long id, @RequestBody Map<String, Double> dest) {
        Object response = routingService.getRouteFromCpp(10, 10, dest.get("x"), dest.get("y"));

        if (response instanceof Map && ((Map<?, ?>) response).containsKey("error")) {
            return "Eroare de conexiune: " + ((Map<?, ?>) response).get("error");
        }
        Map<String, Object> routeData = (Map<String, Object>) response;
        List<Map<String, Integer>> route = (List<Map<String, Integer>>) routeData.get("route");

        movementSimulator.startSimulation(id, route);
        return "Simularea a pornit cu succes!";
    }

    @PostMapping
    public Vehicle createVehicle(@RequestBody Vehicle vehicle) {
        if (vehicle.getStatus() == null) {
            vehicle.setStatus("IDLE");
        }
        return vehicleRepository.save(vehicle);
    }
}
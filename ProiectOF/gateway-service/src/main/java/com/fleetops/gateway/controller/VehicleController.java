package com.fleetops.gateway.controller;

import com.fleetops.gateway.model.Order;
import com.fleetops.gateway.model.RoutePoint;
import com.fleetops.gateway.repository.OrderRepository;
import com.fleetops.gateway.service.MovementSimulator;
import com.fleetops.gateway.model.Vehicle;
import com.fleetops.gateway.repository.VehicleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.fleetops.gateway.service.RoutingService;
import org.springframework.http.MediaType;

import java.util.ArrayList;
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
    MovementSimulator movementSimulator;

    @Autowired
    private OrderRepository orderRepository;

    @PostMapping("/{id}/start-trip")
    public String startTrip(@PathVariable Long id, @RequestBody Map<String, Double> dest) {
        Vehicle v = vehicleRepository.findById(id).orElseThrow();

        Object response = routingService.getRouteFromCpp(v.getCurrentX(), v.getCurrentY(), dest.get("x"), dest.get("y"));

        if (response instanceof Map && ((Map<?, ?>) response).containsKey("error")) {
            return "Eroare: " + ((Map<?, ?>) response).get("error");
        }

        Order order = new Order();
        order.setVehicleId(id);
        order.setStartX(v.getCurrentX());
        order.setStartY(v.getCurrentY());
        order.setEndX(dest.get("x"));
        order.setEndY(dest.get("y"));
        order.setStatus("IN_PROGRESS");

        Map<String, Object> routeData = (Map<String, Object>) response;
        List<Map<String, Object>> routePointsRaw = (List<Map<String, Object>>) routeData.get("route");
        List<Map<String, Double>> routeForSimulator = new ArrayList<>();

        for (Map<String, Object> p : routePointsRaw) {
            double px = ((Number) p.get("x")).doubleValue();
            double py = ((Number) p.get("y")).doubleValue();
            order.getRoute().add(new RoutePoint(null, px, py, order));
            routeForSimulator.add(Map.of("x", px, "y", py));
        }

        orderRepository.save(order);

        v.setStatus("MOVING");
        vehicleRepository.save(v);

        movementSimulator.startSimulation(id, order.getId(), routeForSimulator);

        return "Comanda #" + order.getId() + " a pornit!";
    }

    @PostMapping
    public Vehicle createVehicle(@RequestBody Vehicle vehicle) {
        if (vehicle.getStatus() == null) {
            vehicle.setStatus("IDLE");
        }
        return vehicleRepository.save(vehicle);
    }

    @Autowired
    private com.fleetops.gateway.service.StorageService storageService;

    @PostMapping(value = "/{id}/upload-photo", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public String uploadVehiclePhoto(
            @PathVariable Long id,
            @RequestPart("file") org.springframework.web.multipart.MultipartFile file) {

        vehicleRepository.findById(id).orElseThrow(() -> new RuntimeException("Vehicul inexistent"));

        String fileName = "vehicle_" + id + "_" + file.getOriginalFilename();
        return storageService.uploadFile(fileName, file);
    }
}
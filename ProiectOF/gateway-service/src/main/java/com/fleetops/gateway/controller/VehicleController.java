package com.fleetops.gateway.controller;

import com.fleetops.gateway.dto.TripRequest;
import com.fleetops.gateway.model.Order;
import com.fleetops.gateway.model.RoutePoint;
import com.fleetops.gateway.repository.OrderRepository;
import com.fleetops.gateway.service.MovementSimulator;
import com.fleetops.gateway.model.Vehicle;
import com.fleetops.gateway.repository.VehicleRepository;
import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.fleetops.gateway.service.RoutingService;
import org.springframework.http.MediaType;

import java.util.ArrayList;
import java.util.Map;
import java.util.List;

@RestController
@RequestMapping("/api/vehicles")
public class VehicleController {

    private final Counter orderCounter;

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

    public VehicleController(MeterRegistry registry) {
        this.orderCounter = Counter.builder("fleet.orders.started")
                .description("Numarul total de comenzi lansate")
                .register(registry);
    }

    @PostMapping("/{id}/start-trip")
    public ResponseEntity<String> startTrip(@PathVariable Long id, @RequestBody TripRequest request) {
        Vehicle v = vehicleRepository.findById(id).orElseThrow();

        if (!"IDLE".equals(v.getStatus())) {
            return ResponseEntity.status(400).body("Vehiculul este deja intr-o cursa!");
        }

        Object routeToPickup = routingService.getRouteFromCpp(
                v.getCurrentX(), v.getCurrentY(),
                request.getStartX(), request.getStartY()
        );

        Object routeToDest = routingService.getRouteFromCpp(
                request.getStartX(), request.getStartY(),
                request.getEndX(), request.getEndY()
        );

        if (isError(routeToPickup) || isError(routeToDest)) {
            return ResponseEntity.status(500).body("Eroare la calcularea traseului!");
        }

        Order order = new Order();
        order.setVehicleId(id);
        order.setStartX(request.getStartX());
        order.setStartY(request.getStartY());
        order.setEndX(request.getEndX());
        order.setEndY(request.getEndY());
        order.setStatus("IN_PROGRESS");

        List<Map<String, Double>> finalRouteForSimulator = new ArrayList<>();
        processRouteResponse(routeToPickup, order, finalRouteForSimulator);
        processRouteResponse(routeToDest, order, finalRouteForSimulator);

        orderRepository.save(order);
        v.setStatus("MOVING");
        vehicleRepository.save(v);

        orderCounter.increment();
        movementSimulator.startSimulation(id, order.getId(), finalRouteForSimulator);

        return ResponseEntity.ok("Comanda #" + order.getId() + " a pornit!");
    }

    private void processRouteResponse(Object response, Order order, List<Map<String, Double>> simulatorList) {
        Map<String, Object> routeData = (Map<String, Object>) response;
        List<Map<String, Object>> routePointsRaw = (List<Map<String, Object>>) routeData.get("route");

        for (Map<String, Object> p : routePointsRaw) {
            double px = ((Number) p.get("x")).doubleValue();
            double py = ((Number) p.get("y")).doubleValue();

            order.getRoute().add(new RoutePoint(null, px, py, order));
            simulatorList.add(Map.of("x", px, "y", py));
        }
    }

    private boolean isError(Object response) {
        return response instanceof Map && ((Map<?, ?>) response).containsKey("error");
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
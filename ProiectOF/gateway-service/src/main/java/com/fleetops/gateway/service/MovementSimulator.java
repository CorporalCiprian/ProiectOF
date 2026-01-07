package com.fleetops.gateway.service;

import com.fleetops.gateway.model.Vehicle;
import com.fleetops.gateway.repository.OrderRepository;
import com.fleetops.gateway.repository.VehicleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Map;

@Service
public class MovementSimulator {

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    @Autowired
    private VehicleRepository vehicleRepository;

    @Autowired
    private com.fleetops.gateway.repository.OrderRepository orderRepository;

    @Async
    public void startSimulation(Long vehicleId, Long orderId, List<Map<String, Double>> route) {
        for (Map<String, Double> point : route) {
            try {
                Thread.sleep(2000);

                Vehicle v = vehicleRepository.findById(vehicleId).get();
                // Folosim .doubleValue() pentru siguranță
                v.setCurrentX(point.get("x"));
                v.setCurrentY(point.get("y"));
                v.setStatus("MOVING");
                vehicleRepository.save(v);

                messagingTemplate.convertAndSend("/topic/vehicles", v);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        Vehicle v = vehicleRepository.findById(vehicleId).get();
        v.setStatus("IDLE");
        vehicleRepository.save(v);

        orderRepository.findById(orderId).ifPresent(order -> {
            order.setStatus("COMPLETED");
            orderRepository.save(order);
            System.out.println("Comanda " + orderId + " a fost finalizată cu succes!");
        });
    }
}
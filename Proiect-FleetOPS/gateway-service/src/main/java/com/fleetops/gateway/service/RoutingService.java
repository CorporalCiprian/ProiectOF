package com.fleetops.gateway.service;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import java.util.HashMap;
import java.util.Map;

@Service
public class RoutingService {
    private final RestTemplate restTemplate = new RestTemplate();

    private final String CPP_URL = "http://localhost:8081/calculate-route";

    public Object getRouteFromCpp(double startX, double startY, double endX, double endY) {
        System.out.println("DEBUG: Incerc sa apelez C++ la adresa: " + CPP_URL);
        Map<String, Double> request = new HashMap<>();
        request.put("startX", startX);
        request.put("startY", startY);
        request.put("endX", endX);
        request.put("endY", endY);

        try {
            return restTemplate.postForObject(CPP_URL, request, Object.class);
        } catch (Exception e) {
            return Map.of("error", "Microserviciul C++ nu răspunde! Verifică dacă e pornit în Docker pe portul 8081.");
        }
    }
}
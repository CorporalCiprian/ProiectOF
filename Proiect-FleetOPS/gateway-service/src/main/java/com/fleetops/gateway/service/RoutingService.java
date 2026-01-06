package com.fleetops.gateway.service;

import org.springframework.beans.factory.annotation.Value; // <-- Import important!
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import java.util.HashMap;
import java.util.Map;

@Service
public class RoutingService {
    private final RestTemplate restTemplate = new RestTemplate();

    @Value("${routing.service.url:http://localhost:8081}")
    private String baseUrl;

    public Object getRouteFromCpp(double startX, double startY, double endX, double endY) {
        String fullUrl = baseUrl + "/calculate-route";

        System.out.println("DEBUG: Incerc sa apelez C++ la adresa: " + fullUrl);

        Map<String, Double> request = new HashMap<>();
        request.put("startX", startX);
        request.put("startY", startY);
        request.put("endX", endX);
        request.put("endY", endY);

        try {
            return restTemplate.postForObject(fullUrl, request, Object.class);
        } catch (Exception e) {
            e.printStackTrace();
            return Map.of("error", "Microserviciul C++ nu răspunde la " + fullUrl + ". Eroare: " + e.getMessage());
        }
    }
}
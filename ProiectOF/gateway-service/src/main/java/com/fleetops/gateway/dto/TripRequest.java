package com.fleetops.gateway.dto;

import lombok.Data;

@Data
public class TripRequest {
    private Double startX;
    private Double startY;
    private Double endX;
    private Double endY;
}
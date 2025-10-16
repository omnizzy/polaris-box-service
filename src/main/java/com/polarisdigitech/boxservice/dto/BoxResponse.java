package com.polarisdigitech.boxservice.dto;

import com.polarisdigitech.boxservice.enums.BoxState;
import lombok.Data;

@Data
public class BoxResponse {
    private Long id;
    private String txRef;
    private Double weightLimit;
    private Integer batteryCapacity;
    private BoxState state;
}

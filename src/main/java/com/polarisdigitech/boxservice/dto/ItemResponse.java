package com.polarisdigitech.boxservice.dto;

import lombok.Data;

@Data
public class ItemResponse {
    private Long id;
    private String name;
    private Double weight;
    private String code;
}

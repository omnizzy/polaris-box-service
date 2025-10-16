package com.polarisdigitech.boxservice.dto;

import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class ItemDto {
    @NotBlank
    @Pattern(regexp = "^[A-Za-z0-9_-]+$", message = "name can only contain letters, numbers, hyphen and underscore")
    private String name;

    @NotNull
    @DecimalMin("0.1")
    private Double weight;

    @NotBlank
    @Pattern(regexp = "^[A-Z0-9_]+$", message = "code must be uppercase letters, numbers and underscore only")
    private String code;

}

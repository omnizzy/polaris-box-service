package com.polarisdigitech.boxservice.dto;

import com.polarisdigitech.boxservice.enums.BoxState;
import jakarta.validation.constraints.*;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CreateBoxDto {
    @NotBlank
    @Size(max = 20)
    private String txRef;

    @NotNull
    @DecimalMin("0.1")
    @DecimalMax("500.0")
    private Double weightLimit;

    @NotNull
    @Min(0)
    @Max(100)
    private Integer batteryCapacity;

    private BoxState state = BoxState.IDLE;

}

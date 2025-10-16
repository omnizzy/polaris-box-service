package com.polarisdigitech.boxservice.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

import java.util.List;

@Data
public class LoadItemsRequest {
    @NotEmpty
    @Valid
    private List<ItemDto> items;
}

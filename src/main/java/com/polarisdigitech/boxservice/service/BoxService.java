package com.polarisdigitech.boxservice.service;

import com.polarisdigitech.boxservice.dto.*;
import com.polarisdigitech.boxservice.entity.Item;

import java.util.List;

public interface BoxService {
    BoxResponse createBox(CreateBoxDto dto);
    List<ItemResponse> loadItems(String txRef, LoadItemsRequest request);
    List<ItemResponse> getItemsForBox(String txRef);
    List<BoxResponse> getAvailableBoxes();
    int getBatteryLevel(String txRef);
}

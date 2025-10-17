package com.polarisdigitech.boxservice.controller;

import com.polarisdigitech.boxservice.dto.*;
import com.polarisdigitech.boxservice.service.BoxService;
import jakarta.validation.Valid;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/boxes")
public class BoxController {

    private final BoxService boxService;

    public BoxController(BoxService boxService) {
        this.boxService = boxService;
    }

    @PostMapping("/create_box")
    public ResponseEntity<BoxResponse> createBox(@Valid @RequestBody CreateBoxDto dto) {
        BoxResponse res = boxService.createBox(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(res);
    }

    @PostMapping("/{txRef}/load")
    public ResponseEntity<List<ItemResponse>> loadItems(@PathVariable String txRef,
                                                        @Valid @RequestBody LoadItemsRequest request) {
        List<ItemResponse> items = boxService.loadItems(txRef, request);
        return ResponseEntity.ok(items);
    }

    @GetMapping("/{txRef}/items")
    public ResponseEntity<List<ItemResponse>> getItems(@PathVariable String txRef) {
        return ResponseEntity.ok(boxService.getItemsForBox(txRef));
    }

    @GetMapping("/available")
    public ResponseEntity<List<BoxResponse>> getAvailableBoxes() {
        return ResponseEntity.ok(boxService.getAvailableBoxes());
    }

    @GetMapping("/{txRef}/battery")
    public ResponseEntity<?> getBattery(@PathVariable String txRef) {
        int level = boxService.getBatteryLevel(txRef);
        return ResponseEntity.ok().body(new java.util.HashMap<String, Integer>() {{ put("batteryCapacity", level); }});
    }
}

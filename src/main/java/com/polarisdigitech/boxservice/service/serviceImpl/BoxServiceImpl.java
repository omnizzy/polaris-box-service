package com.polarisdigitech.boxservice.service.serviceImpl;

import com.polarisdigitech.boxservice.dto.*;
import com.polarisdigitech.boxservice.entity.Box;
import com.polarisdigitech.boxservice.entity.Item;
import com.polarisdigitech.boxservice.enums.BoxState;
import com.polarisdigitech.boxservice.exception.BadRequestException;
import com.polarisdigitech.boxservice.exception.ResourceNotFoundException;
import com.polarisdigitech.boxservice.repository.BoxRepository;
import com.polarisdigitech.boxservice.repository.ItemRepository;
import com.polarisdigitech.boxservice.service.BoxService;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class BoxServiceImpl implements BoxService {

    private final BoxRepository boxRepository;
    private final ItemRepository itemRepository;

    public BoxServiceImpl(BoxRepository boxRepository, ItemRepository itemRepository) {
        this.boxRepository = boxRepository;
        this.itemRepository = itemRepository;
    }

    @Override
    public BoxResponse createBox(CreateBoxDto dto) {
        if (boxRepository.findByTxRef(dto.getTxRef()).isPresent()) {
            throw new BadRequestException("txRef must be unique");
        }
        if (dto.getWeightLimit() > 500.0) {
            throw new BadRequestException("weight limit cannot exceed 500g");
        }
        Box box = Box.builder()
                .txRef(dto.getTxRef())
                .weightLimit(dto.getWeightLimit())
                .batteryCapacity(dto.getBatteryCapacity())
                .state(dto.getState() == null ? BoxState.IDLE : dto.getState())
                .build();
        Box saved = boxRepository.save(box);
        return mapToBoxResponse(saved);
    }

    @Override
    @Transactional
    public List<ItemResponse> loadItems(String txRef, LoadItemsRequest request) {
        Box box = boxRepository.findByTxRef(txRef)
                .orElseThrow(() -> new ResourceNotFoundException("Box not found: " + txRef));

        if (box.getBatteryCapacity() < 25) {
            throw new BadRequestException("Cannot load box when battery < 25%");
        }

        if (box.getState() == BoxState.DELIVERING || box.getState() == BoxState.DELIVERED || box.getState() == BoxState.RETURNING) {
            throw new BadRequestException("Box cannot be loaded in its current state: " + box.getState());
        }

        // ensure state becomes LOADING
        box.setState(BoxState.LOADING);

        // check total weight
        double incomingWeight = request.getItems().stream().mapToDouble(ItemDto::getWeight).sum();
        if (!box.canLoadMore(incomingWeight)) {
            throw new BadRequestException("Items exceed box weight limit");
        }

        // Save items
        for (ItemDto dto : request.getItems()) {
            Item item = Item.builder()
                    .name(dto.getName())
                    .weight(dto.getWeight())
                    .code(dto.getCode())
                    .build();
            box.addItem(item);
        }

        // After loading, if now full (or we choose to set LOADED), set LOADED if full or keep LOADING otherwise.
        if (Double.compare(box.getCurrentLoadedWeight(), box.getWeightLimit()) >= 0) {
            box.setState(BoxState.LOADED);
        } else {
            // keep LOADING or decide to mark LOADED based on business rules; we'll keep LOADING
            box.setState(BoxState.LOADING);
        }

        Box savedBox = boxRepository.save(box);
        return savedBox.getItems().stream().map(this::mapToItemResponse).collect(Collectors.toList());
    }

    @Override
    public List<ItemResponse> getItemsForBox(String txRef) {
        Box box = boxRepository.findByTxRef(txRef)
                .orElseThrow(() -> new ResourceNotFoundException("Box not found: " + txRef));
        return box.getItems().stream().map(this::mapToItemResponse).collect(Collectors.toList());
    }

    @Override
    public List<BoxResponse> getAvailableBoxes() {
        // Available means state IDLE or LOADING and battery >= 25
        List<Box> boxes = boxRepository.findByStateInAndBatteryCapacityGreaterThanEqual(List.of(BoxState.IDLE, BoxState.LOADING), 25);
        return boxes.stream().map(this::mapToBoxResponse).collect(Collectors.toList());
    }

    @Override
    public int getBatteryLevel(String txRef) {
        Box box = boxRepository.findByTxRef(txRef).orElseThrow(() -> new ResourceNotFoundException("Box not found: " + txRef));
        return box.getBatteryCapacity();
    }

    // mapping helpers
    private BoxResponse mapToBoxResponse(Box b) {
        BoxResponse r = new BoxResponse();
        r.setId(b.getId());
        r.setTxRef(b.getTxRef());
        r.setWeightLimit(b.getWeightLimit());
        r.setBatteryCapacity(b.getBatteryCapacity());
        r.setState(b.getState());
        return r;
    }

    private ItemResponse mapToItemResponse(Item i) {
        ItemResponse r = new ItemResponse();
        r.setId(i.getId());
        r.setName(i.getName());
        r.setWeight(i.getWeight());
        r.setCode(i.getCode());
        return r;
    }
}

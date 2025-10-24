package com.polarisdigitech.boxservice;

import com.polarisdigitech.boxservice.dto.CreateBoxDto;
import com.polarisdigitech.boxservice.dto.BoxResponse;
import com.polarisdigitech.boxservice.entity.Box;
import com.polarisdigitech.boxservice.enums.BoxState;
import com.polarisdigitech.boxservice.repository.BoxRepository;
import com.polarisdigitech.boxservice.service.serviceImpl.BoxServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BoxServiceImplTest {

    @Mock
    private BoxRepository boxRepository;

    @InjectMocks
    private BoxServiceImpl boxService;

    private CreateBoxDto createBoxDto;

    @BeforeEach
    void setUp() {
        createBoxDto = CreateBoxDto.builder()
                .txRef("BX1000")
                .weightLimit(250.0)
                .batteryCapacity(80)
                .state(BoxState.IDLE)
                .build();
    }

    @Test
    void shouldCreateBoxSuccessfully() {
        // Mock repository behavior
        when(boxRepository.findByTxRef("BX1000")).thenReturn(Optional.empty());
        when(boxRepository.save(any(Box.class))).thenAnswer(invocation -> {
            Box box = invocation.getArgument(0);
            box.setId(1L);
            return box;
        });

        // Call service method
        BoxResponse response = boxService.createBox(createBoxDto);

        // Assert results
        assertNotNull(response);
        assertEquals("BX1000", response.getTxRef());
        assertEquals(BoxState.IDLE, response.getState());
    }
}

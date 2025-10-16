package com.polarisdigitech.boxservice.repository;

import com.polarisdigitech.boxservice.entity.Box;
import com.polarisdigitech.boxservice.enums.BoxState;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface BoxRepository extends JpaRepository<Box, Long> {
    Optional<Box> findByTxRef(String txRef);
    List<Box> findByStateInAndBatteryCapacityGreaterThanEqual(List<BoxState> states, int minBattery);
}

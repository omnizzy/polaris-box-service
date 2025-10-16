package com.polarisdigitech.boxservice.repository;

import com.polarisdigitech.boxservice.entity.Item;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ItemRepository extends JpaRepository<Item, Long> {
    List<Item> findByBoxId(Long boxId);
}

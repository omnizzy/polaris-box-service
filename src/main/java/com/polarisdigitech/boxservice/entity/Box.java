package com.polarisdigitech.boxservice.entity;

import com.polarisdigitech.boxservice.enums.BoxState;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "boxes")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Box {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "txref", length = 20, unique = true, nullable = false)
    private String txRef;

    @Column(name = "weight_limit")
    private Double weightLimit;

    @Column(name = "battery_capacity")
    private Integer batteryCapacity;

    @Enumerated(EnumType.STRING)
    private BoxState state;

    @OneToMany(mappedBy = "box", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<Item> items = new ArrayList<>();

    // helper to compute current loaded weight
    public double getCurrentLoadedWeight() {
        return items.stream().mapToDouble(Item::getWeight).sum();
    }

    public boolean canLoadMore(double additionalWeight) {
        return (getCurrentLoadedWeight() + additionalWeight) <= (this.weightLimit != null ? this.weightLimit : 0);
    }

    public void addItem(Item item) {
        items.add(item);
        item.setBox(this);
    }
}

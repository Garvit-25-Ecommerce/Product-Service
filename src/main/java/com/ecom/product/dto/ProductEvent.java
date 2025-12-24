package com.ecom.product.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductEvent {
    private EventType eventType;
    private String productId;
    private String name;
    private String brand;
    private String category;
    private List<Feature> features;

    private Instant occurredAt;

}

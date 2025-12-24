package com.ecom.product.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.net.URL;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductRequest {
    @NotBlank
    private String name;
    @NotBlank
    private String brand;
    @Positive
    private Double price;
    @NotBlank
    private String category;
    @Positive
    private Integer quantity;
    @NotNull
    private URL imageUrl;
    @NotNull
    @NotEmpty
    private List<Feature> features;
}

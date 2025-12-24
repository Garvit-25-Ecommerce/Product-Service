package com.ecom.product.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductDto {
    private String id;
    private String name;
    private String brand;
    private Double price;
    private String categoryId;
    private Integer quantity = 1;
    private URL imageUrl;
    private List<Feature> features;
    private ArrayList<String> reviews;
}

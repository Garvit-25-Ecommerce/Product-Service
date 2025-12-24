package com.ecom.product.entity;

import com.ecom.product.dto.Feature;
import com.mongodb.lang.NonNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

@Document("Products")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Product {

    @Id
    private String id;
    @NonNull
    @NotBlank(message = "Name field cannot be blank")
    private String name;
    @NonNull
    @NotBlank(message = "Brand field cannot be blank")
    private String brand;
    @NonNull
    @Positive(message = "Price cannot be zero or negative")
    private Double price;
    @NonNull
    @NotBlank(message = "Category cannot be blank")
    private String categoryId;
    @PositiveOrZero(message = "Quantity cannot be negative")
    private Integer quantity = 1;

    private URL imageUrl;
    private List<Feature> features;
    private ArrayList<String> reviews;

    @Override
    public String toString() {
        return "Product{" +
                ", name='" + name + '\'' +
                ", brand='" + brand + '\'' +
                ", price=" + price +
                ", imageUrl=" + imageUrl +
                ", features=" + features +
                '}';
    }
}

package com.ecom.product.dto;

import com.mongodb.lang.NonNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;
import java.net.URL;
import java.util.ArrayList;
import java.util.Map;

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

    private URL imageUrl;
    private Map<String, String> features;
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

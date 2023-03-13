package com.ecom.product.dto;

import com.mongodb.lang.NonNull;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.net.URL;
import java.util.ArrayList;
import java.util.Map;

@Document("Products")
public class Product {

    @Id
    private String id;
    @NonNull
    private String name;
    @NonNull
    private String brand;
    @NonNull
    private Double price;
    @NonNull
    private String category;

    private URL imageUrl;
    private Map<String, String> features;
    private ArrayList<String> reviews;

    public Product(@NonNull String name, @NonNull String brand, @NonNull Double price, @NonNull String category, URL imageUrl, Map<String, String> features) {
        this.name = name;
        this.brand = brand;
        this.price = price;
        this.category = category;
        this.imageUrl = imageUrl;
        this.features = features;

    }

    public ArrayList<String> getReviews() {
        return reviews;
    }

    public void setReviews(ArrayList<String> reviews) {
        this.reviews = reviews;
    }

    public void setId(String id) {
        this.id = id;
    }

    @NonNull
    public String getCategory() {
        return category;
    }

    public void setCategory(@NonNull String category) {
        this.category = category;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public URL getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(URL imageUrl) {
        this.imageUrl = imageUrl;
    }

    public Map<String, String> getFeatures() {
        return features;
    }

    public void setFeatures(Map<String, String> features) {
        this.features = features;
    }

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

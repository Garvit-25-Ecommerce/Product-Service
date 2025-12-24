package com.ecom.product.repository;

import com.ecom.product.entity.Product;
import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;
import java.util.Optional;

public interface ProductRepository extends MongoRepository<Product, String>, ProductRepositoryCustom {
    @Query(value = "{'id': ?0}")
    Optional<Product> findProductById(String id);

    @Aggregation(pipeline = {"{'$sort': {'reviews': -1}}","{'$limit': 5}"})
    Optional<List<Product>> getFeaturedProducts();
}

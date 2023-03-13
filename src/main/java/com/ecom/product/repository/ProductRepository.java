package com.ecom.product.repository;

import com.ecom.product.dto.Product;
import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;
import java.util.Optional;

public interface ProductRepository extends MongoRepository<Product, String> {

    @Query(value = "{'name':?0}")
    Optional<List<Product>> getProductsByName(String name);

    @Query(value = "{'price':{'$gte': ?0,'$lte': ?1}}")
    Optional<List<Product>> getProductsWithinARange(double min, double max);

    @Query(value = "{'id': ?0}")
    Optional<Product> findProductById(String id);

    @Aggregation(pipeline = {"{'$sort': {?0: ?1}}"})
    Optional<List<Product>> sortWithParameter(String sortBy, int ascending);

    @Query(value = "{'category': ?0}")
    Optional<List<Product>> getProductsByCategory(String category);

    @Aggregation(pipeline = {"{'$sort': {'reviews': -1}}","{'$limit': 5}"})
    Optional<List<Product>> getFeaturedProducts();
}

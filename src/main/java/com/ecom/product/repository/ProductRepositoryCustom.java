package com.ecom.product.repository;

import com.ecom.product.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ProductRepositoryCustom {

    Page<Product> getFilteredProducts(Double min, Double max, Pageable pageable, String category, String searchBy) ;
}

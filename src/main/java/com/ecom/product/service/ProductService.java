package com.ecom.product.service;

import com.ecom.commons.ExceptionHandler.DuplicateResourceFoundException;
import com.ecom.commons.ExceptionHandler.ResourceNotFoundException;
import com.ecom.product.dto.Category;
import com.ecom.product.dto.Product;
import com.ecom.product.repository.ProductRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.*;
@Service
@Slf4j
public class ProductService {
    private final ProductRepository productRepository;

    @Autowired
    private final CategoryService categoryService;

    @Autowired
    public ProductService(ProductRepository productRepository, CategoryService categoryService) {
        this.productRepository = productRepository;
        this.categoryService = categoryService;
    }

    private final Integer RESOURCE_NOT_FOUND = 404;
    private final Integer CONFLICT = 409;


    public List<Product> getAllProducts() {
        log.info("ProductService :: getAllProducts :: start");
        List<Product> products = productRepository.findAll();
        if (products.isEmpty()) {
            throw new ResourceNotFoundException("No products in the inventory", RESOURCE_NOT_FOUND);
        }
        log.info("ProductService :: getAllProducts :: end");
        return products;
    }

    public void addProduct(Product product) {
        log.info("ProductService :: addProduct :: start");
        if (productRepository.findProductById(product.getId()).isPresent()) {
            throw new DuplicateResourceFoundException("Product already present", CONFLICT);
        } else {
            String categoryId = categoryService.getCategoryIdByName(product.getCategoryId());
            if (!categoryId.isBlank()) {
                product.setCategoryId(categoryId);
                productRepository.save(product);
            } else {
                throw new ResourceNotFoundException("Product Category does not exist", RESOURCE_NOT_FOUND);
            }
            log.info("ProductService :: addProduct :: end");
        }
    }

    public Page<Product> getFilteredProducts(Double min, Double max, Integer pageNumber,
                                             Integer pageSize, String sortBy, Boolean ascending,
                                             String category, String serachBy){
        log.info("ProductService :: getFilteredProducts :: start");
        Pageable pageable;
        if(null != sortBy && !sortBy.isBlank()) {
            pageable = PageRequest.of(pageNumber,pageSize,ascending? Sort.Direction.ASC: Sort.Direction.DESC,sortBy);
        } else {
            pageable = PageRequest.of(pageNumber,pageSize);
        }

        String categoryId = categoryService.getCategoryIdByName(category);

        Page<Product> products = productRepository.getFilteredProducts(min,max,pageable,categoryId,serachBy);
        if (!products.isEmpty()){
            log.info("ProductService :: getFilteredProducts :: end");
            return products;
        } else {
            throw new ResourceNotFoundException("No products found with specified filters",RESOURCE_NOT_FOUND);
        }

    }

    public void deleteProduct(String id) {
        log.info("ProductService :: deleteProduct :: start");
            if (productRepository.findProductById(id).isPresent()) {
                productRepository.deleteById(id);
                log.info("ProductService :: deleteProduct :: end");
            } else {
                throw new ResourceNotFoundException("No product found",RESOURCE_NOT_FOUND);
            }
    }

    public Product getProductById(String productId) {
        log.info("ProductService :: getProductById :: start");
        Optional<Product> product = productRepository.findProductById(productId);
        if (product.isPresent()) {
            log.info("ProductService :: getProductById :: end");
            product.get().setCategoryId(categoryService.getCategoryNameById(product.get().getCategoryId()));
            return product.get();
        } else {
            throw new ResourceNotFoundException("No product found with specified id", RESOURCE_NOT_FOUND);
        }
    }

    public void addReview(String productId, String review) {
        log.info("ProductService :: addReview :: start");
        Optional<Product> product = productRepository.findProductById(productId);
        if (product.isPresent()) {
            if (product.get().getReviews() == null) {
                product.get().setReviews(new ArrayList<String>());
            }

            product.get().getReviews().add(review);

            productRepository.save(product.get());
            log.info("ProductService :: addReview :: end");
        } else {
            throw new ResourceNotFoundException("Product not found", RESOURCE_NOT_FOUND);
        }
    }

    public List<Product> getFeaturedProducts() {
        log.info("ProductService :: getFeaturedProducts :: start");
        Optional<List<Product>> products = productRepository.getFeaturedProducts();
        if (products.isPresent()) {
            log.info("ProductService :: getFeaturedProducts :: end");
            return products.get();
        } else {
            throw new ResourceNotFoundException("No products found", RESOURCE_NOT_FOUND);
        }
    }


    public void updateProduct(Product product, String id) {
        log.info("ProductService :: updateProduct :: start");
        if (productRepository.findProductById(id).isEmpty()) {
            throw new ResourceNotFoundException("Product not present", RESOURCE_NOT_FOUND);
        } else {
            String categoryId = categoryService.getCategoryIdByName(product.getCategoryId());
            if (!categoryId.isBlank()) {
                product.setCategoryId(categoryId);
                product.setId(id);
                productRepository.save(product);
            } else {
                throw new ResourceNotFoundException("Product Category does not exist", RESOURCE_NOT_FOUND);
            }
            log.info("ProductService :: updateProduct :: end");
        }
    }
}

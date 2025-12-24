package com.ecom.product.service;

import com.ecom.commons.ExceptionHandler.ResourceNotFoundException;
import com.ecom.product.dto.ProductDto;
import com.ecom.product.dto.ProductRequest;
import com.ecom.product.dto.ReviewRequest;
import com.ecom.product.entity.Product;
import com.ecom.product.mapper.ProductMapper;
import com.ecom.product.repository.ProductRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.ecom.product.helper.Constants.RESOURCE_NOT_FOUND;

@Service
@Slf4j
public class ProductService {
    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private ProductMapper productMapper;

    public List<ProductDto> getAllProducts() {
        log.info("ProductService :: getAllProducts :: start");
        List<Product> products = productRepository.findAll();
        if (products.isEmpty()) {
            throw new ResourceNotFoundException("No products in the inventory", RESOURCE_NOT_FOUND);
        }
        List<ProductDto> productDtos = productMapper.toDtoList(products);
        log.info("ProductService :: getAllProducts :: end");
        return productDtos;
    }

    public void addProduct(ProductRequest product) {
        log.info("ProductService :: addProduct :: start");
        String categoryId = categoryService.getCategoryIdByName(product.getCategory());
        if (!categoryId.isBlank()) {
            Product productEntity = productMapper.toEntityFromRequest(product);
            productEntity.setCategoryId(categoryId);
            productRepository.save(productEntity);
        } else {
            throw new ResourceNotFoundException("Product Category does not exist", RESOURCE_NOT_FOUND);
        }
        log.info("ProductService :: addProduct :: end");
    }

    public Page<ProductDto> getFilteredProducts(Double min, Double max, Integer pageNumber,
                                                Integer pageSize, String sortBy, Boolean ascending,
                                                String category, String serachBy) {
        log.info("ProductService :: getFilteredProducts :: start");
        Pageable pageable;
        if (null != sortBy && !sortBy.isBlank()) {
            pageable = PageRequest.of(pageNumber, pageSize, ascending ? Sort.Direction.ASC : Sort.Direction.DESC, sortBy);
        } else {
            pageable = PageRequest.of(pageNumber, pageSize);
        }

        String categoryId = categoryService.getCategoryIdByName(category);

        Page<Product> products = productRepository.getFilteredProducts(min, max, pageable, categoryId, serachBy);
        Page<ProductDto> productDtos = productMapper.toDtoPage(products);
        if (!products.isEmpty()) {
            log.info("ProductService :: getFilteredProducts :: end");
            return productDtos;
        } else {
            throw new ResourceNotFoundException("No products found with specified filters", RESOURCE_NOT_FOUND);
        }

    }

    public void deleteProduct(String id) {
        log.info("ProductService :: deleteProduct :: start");
        if (productRepository.findProductById(id).isPresent()) {
            productRepository.deleteById(id);
            log.info("ProductService :: deleteProduct :: end");
        } else {
            throw new ResourceNotFoundException("No product found", RESOURCE_NOT_FOUND);
        }
    }

    public ProductDto getProductById(String productId) {
        log.info("ProductService :: getProductById :: start");
        Optional<Product> product = productRepository.findProductById(productId);
        if (product.isPresent()) {
            product.get().setCategoryId(categoryService.getCategoryNameById(product.get().getCategoryId()));
            ProductDto productDto = productMapper.toDto(product.get());
            log.info("ProductService :: getProductById :: end");
            return productDto;
        } else {
            throw new ResourceNotFoundException("No product found with specified id", RESOURCE_NOT_FOUND);
        }
    }

    public void addReview(String productId, ReviewRequest reviewRequest) {
        log.info("ProductService :: addReview :: start");
        Optional<Product> product = productRepository.findProductById(productId);
        if (product.isPresent()) {
            if (product.get().getReviews() == null) {
                product.get().setReviews(new ArrayList<String>());
            }

            product.get().getReviews().add(reviewRequest.getReview());

            productRepository.save(product.get());
            log.info("ProductService :: addReview :: end");
        } else {
            throw new ResourceNotFoundException("Product not found", RESOURCE_NOT_FOUND);
        }
    }

    public List<ProductDto> getFeaturedProducts() {
        log.info("ProductService :: getFeaturedProducts :: start");
        Optional<List<Product>> products = productRepository.getFeaturedProducts();
        if (products.isPresent()) {
            log.info("ProductService :: getFeaturedProducts :: end");
            return productMapper.toDtoList(products.get());
        } else {
            throw new ResourceNotFoundException("No products found", RESOURCE_NOT_FOUND);
        }
    }


    public void updateProduct(ProductRequest product, String id) {
        log.info("ProductService :: updateProduct :: start");
        if (productRepository.findProductById(id).isEmpty()) {
            throw new ResourceNotFoundException("Product not present", RESOURCE_NOT_FOUND);
        } else {
            String categoryId = categoryService.getCategoryIdByName(product.getCategory());
            if (!categoryId.isBlank()) {
                Product productEntity = productMapper.toEntityFromRequest(product);
                productEntity.setCategoryId(categoryId);
                productEntity.setId(id);
                productRepository.save(productEntity);
            } else {
                throw new ResourceNotFoundException("Product Category does not exist", RESOURCE_NOT_FOUND);
            }
            log.info("ProductService :: updateProduct :: end");
        }
    }
}

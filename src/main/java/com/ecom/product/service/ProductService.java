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
    private CategoryService categoryService;

    @Autowired
    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }


    public List<Product> getAllProducts() {
        log.info("Entering get all products method in product service");
        List<Product> products = productRepository.findAll();
        if (products.isEmpty()) {
            throw new ResourceNotFoundException("No products in the inventory", 404);
        }
        log.info("Exiting get all products method in product service");
        return products;
    }

    public void addProduct(Product product) {
        log.info("Entering add product method in product service");
        if (productRepository.findProductById(product.getId()).isPresent()) {
            throw new DuplicateResourceFoundException("Product already present", 409);
        } else {
            String categoryId = categoryService.getCategoryIdByName(product.getCategoryId());
            if (!categoryId.isBlank()) {
                product.setCategoryId(categoryId);
                productRepository.save(product);
            } else {
                throw new ResourceNotFoundException("Product Category does not exist", 404);
            }
            log.info("Exiting add product method in product service");
        }
    }

    public Page<Product> getFilteredProducts(Double min, Double max, Integer pageNumber,
                                             Integer pageSize, String sortBy, Boolean ascending,
                                             String category, String serachBy){
        log.info("Entering filter product method in product service");
        Pageable pageable;
        if(null != sortBy && !sortBy.isBlank()) {
            pageable = PageRequest.of(pageNumber,pageSize,ascending? Sort.Direction.ASC: Sort.Direction.DESC,sortBy);
        } else {
            pageable = PageRequest.of(pageNumber,pageSize);
        }

        String categoryId = categoryService.getCategoryIdByName(category);

        Page<Product> products = productRepository.getFilteredProducts(min,max,pageable,categoryId,serachBy);
        if (!products.isEmpty()){
            log.info("Exiting filter product method in product service");
            return products;
        } else {
            throw new ResourceNotFoundException("No products found with specified filters",404);
        }

    }

    public List<Product> getProductsWithSpecifiedName(String name) {
        log.info("Entering get all products with specified name method in product service");
        Optional<List<Product>> products = productRepository.getProductsByName(name);
        try {
            if (products.isPresent()) {
                log.info("Exiting get all products with specified name method in product service");
                return products.get();
            } else {
                throw new Exception("No products with specified name exist");
            }
        } catch (Exception e) {
            log.error("Exception occurred while searching for products by name");
        }
        log.info("Exiting get all products with specified name method in product service");
        return Collections.<Product>emptyList();
    }

    public List<Product> getProductsWithinARange(Double min, Double max) {
        log.info("Entering get products within a specified range method in product service");
        Optional<List<Product>> products = productRepository.getProductsWithinARange(min, max);
        try {
            if (products.isPresent()) {
                log.info("Exiting get products within a specified range method in product service");
                return products.get();
            } else {
                throw new Exception("No products found within this range");
            }

        } catch (Exception e) {
            log.error("Exception occurred while filtering products within a range: " + e.getMessage());
        }

        log.info("Exiting get products within a specified range method in product service");
        return Collections.<Product>emptyList();
    }

    public List<Product> getSortedProducts(String sortBy, Boolean ascending) {
        log.info("Entering get sorted products method in product service");
        Optional<List<Product>> products = productRepository.sortWithParameter(sortBy, ascending ? 1 : -1);
        try {
            if (products.isPresent()) {
                log.info("Exiting get sorted products method in product service");
                return products.get();
            } else {
                throw new Exception("No products in inventory");
            }
        } catch (Exception e) {
            log.error("exception occurred while sorting for products: " + e.getMessage());
        }
        log.info("Exiting get sorted products method in product service");
        return Collections.<Product>emptyList();
    }

    public void deleteProduct(String id) {
        log.info("Entering delete product method in product service");
            if (productRepository.findProductById(id).isPresent()) {
                productRepository.deleteById(id);
                log.info("Exiting delete product method in product service");
            } else {
                throw new ResourceNotFoundException("No product found",404);
            }
    }

    public boolean validateCategory(String category) {
        log.info("Entering validate category method in product service");

        Set<String> categories = new HashSet<>();
        categories.add("Smartphones");
        categories.add("Cameras");
        categories.add("Laptops");
        categories.add("Video Games");
        categories.add("Televisions");
        categories.add("Wearables");

        log.info("Exiting validate category method in product service");
        return categories.contains(category);
    }

    public List<Product> getProductsByCategory(String category) {
        log.info("Entering get product by category method in product service");
        try {
            Optional<List<Product>> products = productRepository.getProductsByCategory(category);
            if (products.isPresent()) {
                log.info("Exiting get product by category method in product service");
                return products.get();
            } else {
                throw new Exception("No products in this category");
            }
        } catch (Exception e) {
            log.error("Exception occurred while fetching products of a category: " + e.getMessage());
        }

        log.info("Exiting get product by category method in product service");
        return Collections.<Product>emptyList();
    }

    public Product getProductById(String productId) {
        log.info("Entering get product by id method in product service");
        Optional<Product> product = productRepository.findProductById(productId);
        if (product.isPresent()) {
            log.info("Exiting get product by id method in product service");
            product.get().setCategoryId(categoryService.getCategoryNameById(product.get().getCategoryId()));
            return product.get();
        } else {
            throw new ResourceNotFoundException("No product found with specified id", 404);
        }
    }

    public void addReview(String productId, String review) {
        log.info("Entering add review method in product services");
        Optional<Product> product = productRepository.findProductById(productId);
        if (product.isPresent()) {
            if (product.get().getReviews() == null) {
                product.get().setReviews(new ArrayList<String>());
            }

            product.get().getReviews().add(review);

            productRepository.save(product.get());
            log.info("Exiting add review method in product services");
        } else {
            throw new ResourceNotFoundException("Product not found", 404);
        }
    }

    public List<Product> getFeaturedProducts() {
        log.info("Entering get featured products method in product services");
        Optional<List<Product>> products = productRepository.getFeaturedProducts();
        if (products.isPresent()) {
            log.info("Exiting get featured products method in product services");
            return products.get();
        } else {
            throw new ResourceNotFoundException("No products found", 404);
        }
    }


}

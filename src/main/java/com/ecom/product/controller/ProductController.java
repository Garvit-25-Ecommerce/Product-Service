package com.ecom.product.controller;

import com.ecom.commons.Dto.CustomResponse;
import com.ecom.commons.ExceptionHandler.CustomizedResponseEntityExceptionHandler;
import com.ecom.commons.ExceptionHandler.ResourceNotFoundException;
import com.ecom.product.dto.FilterProductsRequest;
import com.ecom.product.dto.Product;
import com.ecom.product.service.CategoryService;
import com.ecom.product.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import java.util.*;

@RestController
@CrossOrigin(origins = {"http://localhost:3000"})
@RequestMapping(path = "/product")
@Import({CustomizedResponseEntityExceptionHandler.class})
public class ProductController {
    private final ProductService productService;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping("/all")
    public ResponseEntity<Map<String, Object>> getAllProducts() {
        Map<String, Object> response = new HashMap<>();
        List<Product> products = productService.getAllProducts();
        if (!products.isEmpty()) {
            response.put("data", products);
            response.put("message", "success");
            return new ResponseEntity<>(response, HttpStatus.OK);
        } else {
            response.put("data", Collections.<Product>emptyList());
            throw new ResourceNotFoundException("No products found", 404);
        }
    }

    @GetMapping("/{productId}")
    public ResponseEntity<Map<String, Object>> getProductById(@PathVariable @NotBlank String productId) {
        Map<String, Object> response = new HashMap<>();
        Product product = productService.getProductById(productId);
        if (product != null) {
            response.put("data", product);
            response.put("message", "Success");
            return new ResponseEntity<>(response, HttpStatus.OK);
        } else {
            response.put("data", null);
            throw new ResourceNotFoundException("No product found with specified id", 404);
        }
    }

    //TODO create endpoint for dynamic categories and one for getting filtered products using aggregation
    @PostMapping("/filtered-products")
    public ResponseEntity<Map<String, Object>> getFilteredProducts(@RequestBody @Valid FilterProductsRequest filterProductsRequest) {
        Map<String, Object> response = new HashMap<>();
        Page<Product> products = productService.getFilteredProducts(filterProductsRequest.getMin(),
                filterProductsRequest.getMax(), filterProductsRequest.getPageNumber(), filterProductsRequest.getPageSize(),
                filterProductsRequest.getSortBy(), filterProductsRequest.getAscending(), filterProductsRequest.getCategory(),
                filterProductsRequest.getSearchBy());
        response.put("data", products);
        response.put("message", "success");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping(path = "/featured-products")
    public ResponseEntity<Map<String, Object>> getFeaturedProducts() {
        Map<String, Object> response = new HashMap<>();
        List<Product> products = productService.getFeaturedProducts();
        if (!products.isEmpty()) {
            response.put("data", products);
            response.put("message", "success");
            return new ResponseEntity<>(response, HttpStatus.OK);
        } else {
            response.put("data", null);
            throw new ResourceNotFoundException("No products found", 404);
        }
    }

    @PostMapping(path = "/add-review")
    public ResponseEntity<CustomResponse> addReview(@RequestParam(value = "productId") @NotBlank String productId, @RequestBody Map<String, String> body) {
        if (!body.containsKey("review")) {
            return new ResponseEntity<>(new CustomResponse(false, "Review not added"), HttpStatus.BAD_REQUEST);
        }
        productService.addReview(productId, body.get("review"));
        return new ResponseEntity<>(new CustomResponse(true, "Review added Successfully"), HttpStatus.OK);
    }

    @PostMapping(path = "/add")
    public ResponseEntity<CustomResponse> addProduct(@RequestBody @Valid Product product, @RequestParam(value = "isAdmin") Optional<Boolean> isAdmin) throws Exception {
        if (!isAdmin.isPresent() || !isAdmin.get()) {
            return new ResponseEntity<>(new CustomResponse(false, "You don't have the privileges to add product"), HttpStatus.FORBIDDEN);
        }

        productService.addProduct(product);
        return new ResponseEntity<>(new CustomResponse(true, "Product added successfully"), HttpStatus.CREATED);

    }

    @PutMapping(path = "/update/{id}")
    public ResponseEntity<CustomResponse> updateProduct(@RequestBody @Valid Product product,@PathVariable @NotBlank String id, @RequestParam(value = "isAdmin") Boolean isAdmin) {
        if (null == isAdmin || !isAdmin) {
            return new ResponseEntity<>(new CustomResponse(false, "You don't have the privileges to update this product"), HttpStatus.FORBIDDEN);
        }

        productService.updateProduct(product, id);
        return new ResponseEntity<>(new CustomResponse(true, "Product updated successfully"), HttpStatus.CREATED);
    }

    @DeleteMapping(path = "/delete/{id}")
    public ResponseEntity<CustomResponse> deleteProduct(@PathVariable String id, @RequestParam("isAdmin") Optional<Boolean> isAdmin) {
        if (!isAdmin.isPresent() || !isAdmin.get()) {
            return new ResponseEntity<>(new CustomResponse(false, "You donot have the priveleges to delete product"), HttpStatus.FORBIDDEN);
        }
        productService.deleteProduct(id);
        return new ResponseEntity<>(new CustomResponse(true, "Product deleted successfully"), HttpStatus.OK);
    }
}

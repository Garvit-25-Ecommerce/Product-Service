package com.ecom.product.controller;

import com.ecom.commons.Dto.CustomResponse;
import com.ecom.commons.ExceptionHandler.CustomizedResponseEntityExceptionHandler;
import com.ecom.commons.ExceptionHandler.ResourceNotFoundException;
import com.ecom.product.dto.FilterProductsRequest;
import com.ecom.product.dto.ProductDto;
import com.ecom.product.dto.ProductRequest;
import com.ecom.product.dto.ReviewRequest;
import com.ecom.product.service.ProductService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = {"http://localhost:3000"})
@RequestMapping(path = "/product")
@Import({CustomizedResponseEntityExceptionHandler.class})
public class ProductController {
    private final ProductService productService;

    @Autowired
    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping("/all")
    public ResponseEntity<List<ProductDto>> getAllProducts() {
        List<ProductDto> products = productService.getAllProducts();
        if (!products.isEmpty()) {
            return new ResponseEntity<>(products, HttpStatus.OK);
        } else {
            throw new ResourceNotFoundException("No products found", 404);
        }
    }

    @GetMapping("/{productId}")
    public ResponseEntity<ProductDto> getProductById(@PathVariable @NotBlank String productId) {
        ProductDto product = productService.getProductById(productId);
        if (product != null) {
            return new ResponseEntity<>(product, HttpStatus.OK);
        } else {
            throw new ResourceNotFoundException("No product found with specified id", 404);
        }
    }

    @PostMapping("/filtered-products")
    public ResponseEntity<Page<ProductDto>> getFilteredProducts(@RequestBody @Valid FilterProductsRequest filterProductsRequest) {
        Page<ProductDto> products = productService.getFilteredProducts(filterProductsRequest.getMin(),
                filterProductsRequest.getMax(), filterProductsRequest.getPageNumber(), filterProductsRequest.getPageSize(),
                filterProductsRequest.getSortBy(), filterProductsRequest.getAscending(), filterProductsRequest.getCategory(),
                filterProductsRequest.getSearchBy());
        return new ResponseEntity<>(products, HttpStatus.OK);
    }

    @GetMapping(path = "/featured-products")
    public ResponseEntity<List<ProductDto>> getFeaturedProducts() {
        List<ProductDto> products = productService.getFeaturedProducts();
        if (!products.isEmpty()) {
            return new ResponseEntity<>(products, HttpStatus.OK);
        } else {
            throw new ResourceNotFoundException("No products found", 404);
        }
    }

    @PostMapping(path = "/add-review")
    public ResponseEntity<CustomResponse> addReview(@RequestParam(value = "productId") @NotBlank String productId, @RequestBody @Valid ReviewRequest reviewRequest) {
        productService.addReview(productId, reviewRequest);
        return new ResponseEntity<>(new CustomResponse(true, "Review added Successfully"), HttpStatus.OK);
    }

    @PostMapping(path = "/add")
    public ResponseEntity<CustomResponse> addProduct(@RequestBody @Valid ProductRequest product, @RequestParam(value = "isAdmin", required = false) Boolean isAdmin) {
        if (null == isAdmin || !isAdmin) {
            return new ResponseEntity<>(new CustomResponse(false, "You don't have the privileges to add product"), HttpStatus.FORBIDDEN);
        }

        productService.addProduct(product);
        return new ResponseEntity<>(new CustomResponse(true, "Product added successfully"), HttpStatus.CREATED);

    }

    @PutMapping(path = "/update/{id}")
    public ResponseEntity<CustomResponse> updateProduct(@RequestBody @Valid ProductRequest productRequest,@PathVariable @NotBlank String id, @RequestParam(value = "isAdmin") Boolean isAdmin) {
        if (null == isAdmin || !isAdmin) {
            return new ResponseEntity<>(new CustomResponse(false, "You don't have the privileges to update this product"), HttpStatus.FORBIDDEN);
        }
        productService.updateProduct(productRequest, id);
        return new ResponseEntity<>(new CustomResponse(true, "Product updated successfully"), HttpStatus.CREATED);
    }

    @DeleteMapping(path = "/delete/{id}")
    public ResponseEntity<CustomResponse> deleteProduct(@PathVariable String id, @RequestParam(value = "isAdmin", required = false) Boolean isAdmin) {
        if (null == isAdmin || !isAdmin) {
            return new ResponseEntity<>(new CustomResponse(false, "You don't have the privileges to delete product"), HttpStatus.FORBIDDEN);
        }
        productService.deleteProduct(id);
        return new ResponseEntity<>(new CustomResponse(true, "Product deleted successfully"), HttpStatus.OK);
    }
}

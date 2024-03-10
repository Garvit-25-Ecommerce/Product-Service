package com.ecom.product.controller;

import com.ecom.commons.Dto.CustomResponse;
import com.ecom.commons.ExceptionHandler.ResourceNotFoundException;
import com.ecom.product.dto.Product;
import com.ecom.product.service.CategoryService;
import com.ecom.product.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;
import java.util.*;

@RestController
@CrossOrigin(origins = {"http://localhost:3000"})
@RequestMapping(path = "/product")
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

    @GetMapping("/byId/{productId}")
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

    @GetMapping("/{name}")
    public ResponseEntity<Map<String, Object>> getProductsWithSpecifiedName(@PathVariable @NotBlank String name) {
        Map<String, Object> response = new HashMap<>();
        List<Product> products = productService.getProductsWithSpecifiedName(name);
        if (!products.isEmpty()) {
            response.put("data", products);
            response.put("message", "success");
            return new ResponseEntity<>(response, HttpStatus.OK);
        } else {
            response.put("data", Collections.<Product>emptyList());
            throw new ResourceNotFoundException("No products with specified name found", 404);
        }
    }

    //TODO create endpoint for dynamic categories and one for getting filtered products using aggregation
    @GetMapping("/filteredProducts")
    public ResponseEntity<Map<String, Object>> getFilteredProducts(@RequestParam(value = "min") Optional<Double> min,
                                                                   @RequestParam(value = "max") Optional<Double> max,
                                                                   @RequestParam(value = "ascending") Optional<Boolean> ascending,
                                                                   @RequestParam(value = "sortBy") Optional<String> sortBy,
                                                                   @RequestParam(value = "searchBy") Optional<String> searchBy,
                                                                   @RequestParam(value = "pageNumber") Optional<Integer> pageNumber,
                                                                   @RequestParam(value = "pageSize") Optional<Integer> pageSize,
                                                                   @RequestParam(value = "category") Optional<String> category) {
        Map<String, Object> response = new HashMap<>();
        Page<Product> products = productService.getFilteredProducts(min.orElse(0d),
                max.orElse(Double.MAX_VALUE), pageNumber.orElse(0), pageSize.orElse(10), sortBy.orElse(""),
                ascending.orElse(true), category.orElse(""), searchBy.orElse(""));
        response.put("data", products);
        response.put("message", "success");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<Map<String, Object>> getProductsWithinARange(@RequestParam(value = "min") Optional<Double> min, @RequestParam(value = "max") Optional<Double> max) {
        Map<String, Object> response = new HashMap<>();
        try {
            List<Product> products = productService.getProductsWithinARange(min.orElse(0.0), max.orElse(Double.MAX_VALUE));
            if (!products.isEmpty()) {
                response.put("data", products);
                response.put("message", "success");
                return new ResponseEntity<>(response, HttpStatus.OK);
            } else {
                response.put("data", Collections.<Product>emptyList());
                throw new Exception("No products found within the specified range");
            }
        } catch (Exception e) {
            response.put("message", e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/sortedProducts")
    public ResponseEntity<Map<String, Object>> getSortedProducts(@RequestParam(value = "ascending") Boolean ascending, @RequestParam(value = "sortBy") String sortBy) {
        Map<String, Object> response = new HashMap<>();
        try {
            List<Product> products = productService.getSortedProducts(sortBy, ascending);
            if (!products.isEmpty()) {
                response.put("data", products);
                response.put("message", "success");
                return new ResponseEntity<>(response, HttpStatus.OK);
            } else {
                response.put("data", Collections.<Product>emptyList());
                throw new Exception("No products found");
            }
        } catch (Exception e) {
            response.put("message", e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/byCategory")
    public ResponseEntity<Map<String, Object>> getProductsByCategory(@RequestParam(value = "category") String category) {
        Map<String, Object> response = new HashMap<>();
        try {
            if (!productService.validateCategory(category)) {
                throw new Exception("Entered category is not valid");
            }

            List<Product> products = productService.getProductsByCategory(category);

            if (!products.isEmpty()) {
                response.put("data", products);
                response.put("message", "success");
                return new ResponseEntity<>(response, HttpStatus.OK);
            } else {
                response.put("data", Collections.<Product>emptyList());
                throw new Exception("No products found in the specified category");
            }
        } catch (Exception e) {
            response.put("message", e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping(path = "/featuredProducts")
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

    @PostMapping(path = "/addReview")
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

    @DeleteMapping(path = "/delete/{id}")
    public ResponseEntity<CustomResponse> deleteProduct(@PathVariable String id, @RequestParam("isAdmin") Optional<Boolean> isAdmin) {
        if (!isAdmin.isPresent() || !isAdmin.get()) {
            return new ResponseEntity<>(new CustomResponse(false, "You donot have the priveleges to delete product"), HttpStatus.FORBIDDEN);
        }
        productService.deleteProduct(id);
        return new ResponseEntity<>(new CustomResponse(true, "Product deleted successfully"), HttpStatus.OK);
    }
}

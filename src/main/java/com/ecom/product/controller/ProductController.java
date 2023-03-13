package com.ecom.product.controller;

import com.ecom.product.dto.CustomResponse;
import com.ecom.product.dto.Product;
import com.ecom.product.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@CrossOrigin(origins = {"http://localhost:3000"})
@RequestMapping(path = "/product")
public class ProductController {
    private final ProductService productService;

    @Autowired
    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping("/all")
    public ResponseEntity<Map<String, Object>> getAllProducts() {
        Map<String, Object> response = new HashMap<>();
        try {
            List<Product> products = productService.getAllProducts();
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

    @GetMapping("/byId/{productId}")
    public ResponseEntity<Map<String, Object>> getProductById(@PathVariable String productId) {
        Map<String, Object> response = new HashMap<>();
        try {
            Product product = productService.getProductById(productId);
            if (product != null) {
                response.put("data", product);
                response.put("message", "Success");
                return new ResponseEntity<>(response, HttpStatus.OK);
            } else {
                response.put("data", null);
                throw new Exception("No product found with specified id");
            }
        } catch (Exception e) {
            response.put("message", e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/{name}")
    public ResponseEntity<Map<String, Object>> getProductsWithSpecifiedName(@PathVariable String name) {
        Map<String, Object> response = new HashMap<>();
        try {
            List<Product> products = productService.getProductsWithSpecifiedName(name);
            if (!products.isEmpty()) {
                response.put("data", products);
                response.put("message", "success");
                return new ResponseEntity<>(response, HttpStatus.OK);
            } else {
                response.put("data", Collections.<Product>emptyList());
                throw new Exception("No products with specified name found");
            }
        } catch (Exception e) {
            response.put("message", e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }
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
    public ResponseEntity<Map<String,Object>> getFeaturedProducts(){
        Map<String,Object> response = new HashMap<>();
        try{
            List<Product> products = productService.getFeaturedProducts();
            if(!products.isEmpty()){
                response.put("data",products);
                response.put("message","success");
                return new ResponseEntity<>(response,HttpStatus.OK);
            }else{
                response.put("data",null);
                throw new Exception("No products found");
            }
        }catch (Exception e){
            response.put("message",e.getMessage());
            return new ResponseEntity<>(response,HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping(path = "/addReview")
    public ResponseEntity<CustomResponse> addReview(@RequestParam(value = "productId") String productId ,@RequestBody Map<String,String> body) {
        try {
            if(!body.containsKey("review")){
                return new ResponseEntity<>(new CustomResponse(false,"Review not added"),HttpStatus.BAD_REQUEST);
            }
            boolean result = productService.addReview(productId,body.get("review"));
            if (result) {
                return new ResponseEntity<>(new CustomResponse(result, "Review added Successfully"), HttpStatus.OK);
            } else {
                throw new Exception("Product Not Found");
            }

        } catch (Exception e) {
            return new ResponseEntity<>(new CustomResponse(false,e.getMessage()),HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping(path = "/add")
    public ResponseEntity<CustomResponse> addProduct(@RequestBody Product product, @RequestParam(value = "isAdmin") Optional<Boolean> isAdmin) {
        try {
            if (!isAdmin.isPresent() || !isAdmin.get()) {
                return new ResponseEntity<>(new CustomResponse(false, "You donot have the priveleges to add product"), HttpStatus.FORBIDDEN);
            }
            if (product.getName().length() == 0) {
                throw new Exception("Name field cannot be empty");
            }
            if (product.getPrice() <= 0) {
                throw new Exception("Price cannot be zero or negative");
            }
            if (product.getBrand().length() == 0) {
                throw new Exception("Brand field cannot be empty");
            }
            if (!productService.validateCategory(product.getCategory())) {
                throw new Exception("Invalid category entered, Valid categories are:\nSmartphone" +
                        "\nCameras" +
                        "\nLaptops" +
                        "\nVideo Games" +
                        "\nTelevision"+
                        "\nWearables");
            }

            boolean result = productService.addProduct(product);

            if (result) {
                return new ResponseEntity<>(new CustomResponse(result, "Product added successfully"), HttpStatus.CREATED);
            } else {
                throw new Exception("Cannot add product");
            }
        } catch (Exception e) {
            return new ResponseEntity<>(new CustomResponse(false, e.getMessage()), HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping(path = "/delete/{id}")
    public ResponseEntity<CustomResponse> deleteProduct(@PathVariable String id, @RequestParam("isAdmin") Optional<Boolean> isAdmin) {
        try {
            if (!isAdmin.isPresent() || !isAdmin.get()) {
                return new ResponseEntity<>(new CustomResponse(false, "You donot have the priveleges to delete product"), HttpStatus.FORBIDDEN);
            }

            boolean result = productService.deleteProduct(id);

            if (result) {
                return new ResponseEntity<>(new CustomResponse(result, "Product deleted successfully"), HttpStatus.OK);
            } else {
                throw new Exception();
            }
        } catch (Exception e) {
            return new ResponseEntity<>(new CustomResponse(false, "product does not exist"), HttpStatus.BAD_REQUEST);
        }
    }
}

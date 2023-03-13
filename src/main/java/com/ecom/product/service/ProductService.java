package com.ecom.product.service;

import com.ecom.product.dto.Product;
import com.ecom.product.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

@Service
public class ProductService {
    private final ProductRepository productRepository;
    private Logger logger = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);

    @Autowired
    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }


    public List<Product> getAllProducts() {
        logger.log(Level.INFO, "Entering get all products method in product service");
        try {
            List<Product> products = productRepository.findAll();
            if (products.isEmpty()) {
                throw new Exception("No products in the inventory");
            }
            logger.log(Level.INFO, "Exiting get all products method in product service");
            return products;
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Exception occurred while getting all products: " + e.getMessage());
        }
        logger.log(Level.INFO, "Exiting get all products method in product service");
        return Collections.<Product>emptyList();
    }

    public Boolean addProduct(Product product) {
        logger.log(Level.INFO, "Entering add product method in product service");
        try {
            if (productRepository.findProductById(product.getId()).isPresent()) {
                throw new Exception("Product already present");
            } else {
                if (validateCategory(product.getCategory())) {
                    productRepository.save(product);
                } else {
                    throw new Exception("Product Category does not exist");
                }
                logger.log(Level.INFO, "Exiting add product method in product service");
                return true;
            }
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Exception occurred while adding product: " + e.getMessage());
        }

        logger.log(Level.INFO, "Exiting add product method in product service");
        return false;
    }

    public List<Product> getProductsWithSpecifiedName(String name) {
        logger.log(Level.INFO, "Entering get all products with specified name method in product service");
        Optional<List<Product>> products = productRepository.getProductsByName(name);
        try {
            if (products.isPresent()) {
                logger.log(Level.INFO, "Exiting get all products with specified name method in product service");
                return products.get();
            } else {
                throw new Exception("No products with specified name exist");
            }
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Exception occurred while searching for products by name");
        }
        logger.log(Level.INFO, "Exiting get all products with specified name method in product service");
        return Collections.<Product>emptyList();
    }

    public List<Product> getProductsWithinARange(Double min, Double max) {
        logger.log(Level.INFO, "Entering get products within a specified range method in product service");
        Optional<List<Product>> products = productRepository.getProductsWithinARange(min, max);
        try {
            if (products.isPresent()) {
                logger.log(Level.INFO, "Exiting get products within a specified range method in product service");
                return products.get();
            } else {
                throw new Exception("No products found within this range");
            }

        } catch (Exception e) {
            logger.log(Level.SEVERE, "Exception occurred while filtering products within a range: " + e.getMessage());
        }

        logger.log(Level.INFO, "Exiting get products within a specified range method in product service");
        return Collections.<Product>emptyList();
    }

    public List<Product> getSortedProducts(String sortBy, Boolean ascending) {
        logger.log(Level.INFO, "Entering get sorted products method in product service");
        Optional<List<Product>> products = productRepository.sortWithParameter(sortBy, ascending ? 1 : -1);
        try {
            if (products.isPresent()) {
                logger.log(Level.INFO, "Exiting get sorted products method in product service");
                return products.get();
            } else {
                throw new Exception("No products in inventory");
            }
        } catch (Exception e) {
            logger.log(Level.SEVERE, "exception occurred while sorting for products: " + e.getMessage());
        }
        logger.log(Level.INFO, "Exiting get sorted products method in product service");
        return Collections.<Product>emptyList();
    }

    public boolean deleteProduct(String id) {
        logger.log(Level.INFO, "Entering delete product method in product service");
        try {
            if (productRepository.findProductById(id).isPresent()) {
                productRepository.deleteById(id);
                logger.log(Level.INFO, "Exiting delete product method in product service");
                return true;
            } else {
                throw new Exception("No product found");
            }
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Exception occurred while deleting product: " + e.getMessage());
        }
        logger.log(Level.INFO, "Exiting delete product method in product service");
        return false;
    }

    public boolean validateCategory(String category) {
        logger.log(Level.INFO, "Entering validate category method in product service");

        Set<String> categories = new HashSet<>();
        categories.add("Smartphones");
        categories.add("Cameras");
        categories.add("Laptops");
        categories.add("Video Games");
        categories.add("Televisions");
        categories.add("Wearables");

        logger.log(Level.INFO, "Exiting validate category method in product service");
        return categories.contains(category);
    }

    public List<Product> getProductsByCategory(String category) {
        logger.log(Level.INFO, "Entering get product by category method in product service");
        try {
            Optional<List<Product>> products = productRepository.getProductsByCategory(category);
            if (products.isPresent()) {
                logger.log(Level.INFO, "Exiting get product by category method in product service");
                return products.get();
            } else {
                throw new Exception("No products in this category");
            }
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Exception occurred while fetching products of a category: " + e.getMessage());
        }

        logger.log(Level.INFO, "Exiting get product by category method in product service");
        return Collections.<Product>emptyList();
    }

    public Product getProductById(String productId) {
        logger.log(Level.INFO, "Entering get product by id method in product service");
        try {
            Optional<Product> product = productRepository.findProductById(productId);
            if (product.isPresent()) {
                logger.log(Level.INFO, "Exiting get product by id method in product service");
                return product.get();
            } else {
                throw new Exception("No product found with specified id");
            }
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Exception occurred while retrieving product by id: " + e.getMessage());
        }
        logger.log(Level.INFO, "Exiting get product by id method in product service");
        return null;
    }

    public boolean addReview(String productId,String review) {
        logger.log(Level.INFO,"Entering add review method in product services");
        try{
            Optional<Product> product = productRepository.findProductById(productId);
            if(product.isPresent()){
                if(product.get().getReviews()==null){
                    product.get().setReviews(new ArrayList<String>());
                }

                product.get().getReviews().add(review);

                productRepository.save(product.get());
                logger.log(Level.INFO,"Exiting add review method in product services");
                return true;
            }else{
                throw new Exception("Product not found");
            }
        }catch (Exception e){
            logger.log(Level.SEVERE,"Exception occurred while adding review for a product: "+e.getMessage());
        }
        logger.log(Level.INFO,"Exiting add review method in product services");
        return false;
    }

    public List<Product> getFeaturedProducts() {
        logger.log(Level.INFO,"Entering get featured products method in product services");
        try{
            Optional<List<Product>> products = productRepository.getFeaturedProducts();
            if(products.isPresent()){
                logger.log(Level.INFO,"Exiting get featured products method in product services");
                return products.get();
            }else{
                throw new Exception("No products found");
            }
        }catch(Exception e){
            logger.log(Level.SEVERE,"Exception occurred while fetching featured products: "+e.getMessage());
        }
        logger.log(Level.INFO,"Exiting get featured products method in product services");
        return Collections.<Product>emptyList();
    }
}

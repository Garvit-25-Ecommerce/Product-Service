package com.ecom.product.service;

import com.ecom.commons.ExceptionHandler.DuplicateResourceFoundException;
import com.ecom.commons.ExceptionHandler.ResourceNotFoundException;
import com.ecom.product.dto.Category;
import com.ecom.product.repository.CategoryRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

import static com.ecom.product.helper.Constants.CONFLICT;
import static com.ecom.product.helper.Constants.RESOURCE_NOT_FOUND;

@Service
@Slf4j
public class CategoryService {
    @Autowired
    private final CategoryRepository categoryRepository;

    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    public void addCategory(Category category) {
        log.info("Category Service :: addCategory :: start");
        if (categoryRepository.findByName(category.getName()).isPresent()) {
            throw new DuplicateResourceFoundException("Category with same name already exists",CONFLICT);
        }
        log.info("Category Service :: addCategory :: end");
        categoryRepository.save(category);
    }

    public List<Category> getCategories() {
        log.info("Category Service :: getCategories :: start");
        List<Category> categories = categoryRepository.findAll();
        if(categories.isEmpty()){
            throw new ResourceNotFoundException("No categories found",RESOURCE_NOT_FOUND);
        }
        log.info("Category Service :: getCategories :: end");
        return categories;
    }

    public String getCategoryIdByName (String name) {
        log.info("Category Service :: getCategoryIdByName :: start");
        Optional<Category> category = categoryRepository.findByName(name);
        log.info("Category Service :: getCategoryIdByName :: end");
        return category.map(Category::getId).orElse("");
    }

    public String getCategoryNameById (String id) {
        log.info("Category Service :: getCategoryNameById :: start");
        Optional<Category> category = categoryRepository.findById(id);
        log.info("Category Service :: getCategoryNameById :: end");
        return category.map(Category::getName).orElse("");
    }

    public void deleteCategory(String id) {
        log.info("Category Service :: deleteCategory :: start");
        categoryRepository.deleteById(id);
        log.info("Category Service :: deleteCategory :: end");
    }
}

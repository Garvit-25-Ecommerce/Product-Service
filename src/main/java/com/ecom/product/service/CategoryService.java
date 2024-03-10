package com.ecom.product.service;

import com.ecom.commons.ExceptionHandler.DuplicateResourceFoundException;
import com.ecom.commons.ExceptionHandler.ResourceNotFoundException;
import com.ecom.product.dto.Category;
import com.ecom.product.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CategoryService {
    @Autowired
    private CategoryRepository categoryRepository;

    public void addCategory(Category category) {
        if (categoryRepository.findByName(category.getName()).isPresent()) {
            throw new DuplicateResourceFoundException("Category with same name already exists",409);
        }

        categoryRepository.save(category);
    }

    public List<Category> getCategories() {
        List<Category> categories = categoryRepository.findAll();

        if(null == categories && categories.isEmpty()){
            throw new ResourceNotFoundException("No categories found",404);
        }

        return categories;
    }

    public String getCategoryIdByName (String name) {
        Optional<Category> category = categoryRepository.findByName(name);
        if(category.isPresent()) {
            return category.get().getId();
        }
        return "";
    }

    public String getCategoryNameById (String id) {
        Optional<Category> category = categoryRepository.findById(id);
        if(category.isPresent()) {
            return category.get().getName();
        }
        return "";
    }
}

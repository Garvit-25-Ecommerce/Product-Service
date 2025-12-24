package com.ecom.product.controller;

import com.ecom.commons.Dto.CustomResponse;
import com.ecom.product.entity.Category;
import com.ecom.product.service.CategoryService;
import jakarta.validation.constraints.NotBlank;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;

@RestController
@CrossOrigin(origins = {"http://localhost:3000"})
@RequestMapping(path = "/category")
public class CategoryController {
    @Autowired
    private CategoryService categoryService;

    @PostMapping("/add")
    public ResponseEntity<CustomResponse> addCategory (@RequestBody @Valid Category category) {
        categoryService.addCategory(category);
        return new ResponseEntity<>(new CustomResponse(true,"Category added successfully"), HttpStatus.OK);
    }

    @GetMapping("/all")
    public ResponseEntity<List<Category>> getCategories(){
        List<Category> categories = categoryService.getCategories();
        return new ResponseEntity<>(categories,HttpStatus.OK);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<CustomResponse> deleteCategory (@PathVariable(name = "id") @NotBlank String id) {
        categoryService.deleteCategory(id);
        return  new ResponseEntity<>(new CustomResponse(true,"Category deleted successfully"), HttpStatus.OK);
    }

}

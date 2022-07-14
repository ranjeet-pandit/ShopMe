package com.cognizant.backend.service.impl;

import com.cognizant.backend.exception.ResourceNotFoundException;
import com.cognizant.backend.model.Category;
import com.cognizant.backend.payload.CategoryDto;
import com.cognizant.backend.repository.CategoryRepository;
import com.cognizant.backend.service.CategoryService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CategoryServiceImpl implements CategoryService {

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public CategoryDto createCategory(CategoryDto categoryDto) {

        Category category = this.dtoToCategory(categoryDto);
        category.setImageUrl("defaultCategory.png");
        Category savedCategory = this.categoryRepository.save(category);

        return this.categoryToDto(savedCategory);
    }

    @Override
    public CategoryDto updateCategory(CategoryDto categoryDto, long categoryId) {

        Category category = this.categoryRepository.findById(categoryId).orElseThrow(() -> new ResourceNotFoundException("Category" , "id" , categoryId));

        category.setImageUrl(categoryDto.getImageUrl());

        Category updatedCategory = this.categoryRepository.save(category);

        return this.categoryToDto(updatedCategory);
    }

    @Override
    public CategoryDto getCategoryById(long categoryId) {
        Category category = this.categoryRepository.findById(categoryId).orElseThrow(() -> new ResourceNotFoundException("Category" , "id" , categoryId));
        return this.categoryToDto(category);
    }

    @Override
    public List<CategoryDto> getAllCategories() {
        List<Category> users = this.categoryRepository.findAll();

        return users.stream().map(category -> this.categoryToDto(category)).collect(Collectors.toList());
    }

    @Override
    public void deleteCategory(long categoryId) {
        Category category = this.categoryRepository.findById(categoryId).orElseThrow(() -> new ResourceNotFoundException("Category" , "id" , categoryId));

        this.categoryRepository.delete(category);
    }

    private Category dtoToCategory(CategoryDto categoryDto){
        return this.modelMapper.map(categoryDto, Category.class);
    }

    private CategoryDto categoryToDto(Category category){
        return this.modelMapper.map(category, CategoryDto.class);
    }
}

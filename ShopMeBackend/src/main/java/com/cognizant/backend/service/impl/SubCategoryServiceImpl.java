package com.cognizant.backend.service.impl;

import com.cognizant.backend.exception.ResourceNotFoundException;
import com.cognizant.backend.model.Category;
import com.cognizant.backend.model.SubCategory;
import com.cognizant.backend.payload.SubCategoryDto;
import com.cognizant.backend.repository.CategoryRepository;
import com.cognizant.backend.repository.SubCategoryRepository;
import com.cognizant.backend.service.SubCategoryService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class SubCategoryServiceImpl implements SubCategoryService {

    @Autowired
    private SubCategoryRepository subCategoryRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public SubCategoryDto createSubCategory(SubCategoryDto subCategoryDto , long categoryId) {

        Category category = this.categoryRepository.findById(categoryId).orElseThrow(() -> new ResourceNotFoundException("Category" , "categoryId" , categoryId));;

        SubCategory subCategory = this.dtoToSubCategory(subCategoryDto);

        subCategory.setCategory(category);
        subCategory.setImageUrl("defaultCategory.png");
        SubCategory savedSubCategory = this.subCategoryRepository.save(subCategory);

        return this.subCategoryToDto(savedSubCategory);
    }

    @Override
    public SubCategoryDto updateSubCategory(SubCategoryDto subCategoryDto, long subCategoryId) {
        SubCategory subCategory = this.subCategoryRepository.findById(subCategoryId).orElseThrow(() -> new ResourceNotFoundException("SubCategory" , "subCategoryId" , subCategoryId));

        subCategory.setTitle(subCategoryDto.getTitle());
        subCategory.setImageUrl(subCategoryDto.getImageUrl());
        SubCategory updatedSubCategory = this.subCategoryRepository.save(subCategory);
        return this.subCategoryToDto(updatedSubCategory);
    }

    @Override
    public SubCategoryDto getSubCategoryById(long subCategoryId) {
        SubCategory subCategory = this.subCategoryRepository.findById(subCategoryId).orElseThrow(() -> new ResourceNotFoundException("SubCategory" , "subCategoryId" , subCategoryId));

        return this.subCategoryToDto(subCategory);
    }

    @Override
    public List<SubCategoryDto> getAllSubCategories() {

        List<SubCategory> subCategories = this.subCategoryRepository.findAll();

        return subCategories.stream().map(subCategory -> this.subCategoryToDto(subCategory)).collect(Collectors.toList());
    }

    @Override
    public void deleteSubCategory(long subCategoryId) {
        SubCategory subCategory = this.subCategoryRepository.findById(subCategoryId).orElseThrow(() -> new ResourceNotFoundException("SubCategory" , "subCategoryId" , subCategoryId));

        this.subCategoryRepository.delete(subCategory);
    }

    @Override
    public List<SubCategoryDto> getSubCategoryByCategory(long categoryId) {

        Category category = this.categoryRepository.findById(categoryId).orElseThrow(() -> new ResourceNotFoundException("Category" , "categoryId" , categoryId));;

        List<SubCategory> subCategories =  this.subCategoryRepository.findByCategory(category);

        return  subCategories.stream().map((subCategory -> this.subCategoryToDto(subCategory))).collect(Collectors.toList());
    }

    private SubCategory dtoToSubCategory(SubCategoryDto subCategoryDto){
        return this.modelMapper.map(subCategoryDto, SubCategory.class);
    }

    private SubCategoryDto subCategoryToDto(SubCategory subCategory){
        return this.modelMapper.map(subCategory, SubCategoryDto.class);
    }
}

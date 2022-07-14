package com.cognizant.backend.service;

import com.cognizant.backend.payload.SubCategoryDto;

import java.util.List;

public interface SubCategoryService {

    SubCategoryDto createSubCategory(SubCategoryDto subCategoryDto , long categoryId);

    SubCategoryDto updateSubCategory(SubCategoryDto subCategoryDto , long subCategoryId);

    SubCategoryDto getSubCategoryById(long subCategoryId);

    List<SubCategoryDto> getAllSubCategories();

    void deleteSubCategory(long subCategoryId);

    //get all subcategories by categoryId
    List<SubCategoryDto> getSubCategoryByCategory(long categoryId);

}

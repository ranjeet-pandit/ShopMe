package com.cognizant.backend.service;

import com.cognizant.backend.payload.ProductResponse;
import com.cognizant.backend.payload.ProductDto;

public interface ProductService {

    ProductDto createProduct(ProductDto productDto , long categoryId , long subCategoryId);

    ProductDto updateProduct(ProductDto productDto, long productId);

    ProductDto getProductById(long productId);

    ProductResponse getAllProducts(Integer pageNumber , Integer pageSize , String sortBy , String sortDir);

    void deleteProduct(long productId);

    //get all products by subCategoryId
    ProductResponse getProductsBySubCategory(long subCategoryId , Integer pageNumber , Integer pageSize ,  String sortBy, String sortDir);

    //get all products by categoryId
    ProductResponse getProductsByCategory(long categoryId , Integer pageNumber , Integer pageSize ,  String sortBy, String sortDir);
}
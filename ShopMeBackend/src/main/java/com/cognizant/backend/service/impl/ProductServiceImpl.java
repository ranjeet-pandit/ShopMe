package com.cognizant.backend.service.impl;

import com.cognizant.backend.exception.ResourceNotFoundException;
import com.cognizant.backend.model.Category;
import com.cognizant.backend.model.Product;
import com.cognizant.backend.model.SubCategory;
import com.cognizant.backend.payload.ProductResponse;
import com.cognizant.backend.payload.ProductDto;
import com.cognizant.backend.repository.CategoryRepository;
import com.cognizant.backend.repository.ProductRepository;
import com.cognizant.backend.repository.SubCategoryRepository;
import com.cognizant.backend.service.ProductService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductServiceImpl implements ProductService {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private SubCategoryRepository subCategoryRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private ModelMapper modelMapper;


    @Override
    public ProductDto createProduct(ProductDto productDto , long categoryId , long subCategoryId) {

        Category category = this.categoryRepository.findById(categoryId).orElseThrow(() -> new ResourceNotFoundException("Category" , "categoryId" , categoryId));;

        SubCategory subCategory = this.subCategoryRepository.findById(subCategoryId).orElseThrow(() -> new ResourceNotFoundException("SubCategory" , "subCategoryId" , subCategoryId));;

        Product product = this.dtoToProduct(productDto);


        product.setCategory(category);
        product.setSubCategory(subCategory);
        product.setImageUrl("defaultProduct.jpg");
        product.setTimestamp(new Date());

        Product savedProduct = this.productRepository.save(product);

        return this.productToDto(savedProduct);
    }

    @Override
    public ProductDto updateProduct(ProductDto productDto, long productId) {

        Product product = this.productRepository.findById(productId).orElseThrow(() -> new ResourceNotFoundException("Product" , "productId" , productId));

        product.setName(productDto.getName());
        product.setCompanyName(productDto.getCompanyName());
        product.setDescription(productDto.getDescription());
        product.setActualPrice(productDto.getActualPrice());


        product.setPrice(productDto.getPrice());

        product.setImageUrl(productDto.getImageUrl());
        product.setTimestamp(new Date());

        Product updatedProduct = this.productRepository.save(product);

        return this.productToDto(updatedProduct);
    }

    @Override
    public ProductDto getProductById(long productId) {
        Product product = this.productRepository.findById(productId).orElseThrow(() -> new ResourceNotFoundException("Product" , "productId" , productId));
        return this.productToDto(product);
    }

    @Override
    public ProductResponse getAllProducts(Integer pageNumber , Integer pageSize , String sortBy , String sortDir) {

        Pageable pageable = PageRequest.of(pageNumber , pageSize , (sortDir.equals("asc")) ?  Sort.by(sortBy).ascending() :  Sort.by(sortBy).descending());

        Page<Product> pageProducts = this.productRepository.findAll(pageable);

        List<Product> products = pageProducts.getContent();

        List<ProductDto> productDtoList = products.stream().map(product -> this.productToDto(product)).collect(Collectors.toList());

        ProductResponse productResponse = new ProductResponse();
        productResponse.setProductList(productDtoList);
        productResponse.setPageNumber(pageProducts.getNumber());
        productResponse.setPageSize(pageProducts.getSize());
        productResponse.setTotalElement(pageProducts.getTotalElements());
        productResponse.setTotalPages(pageProducts.getTotalPages());
        productResponse.setLastPage(pageProducts.isLast());

        return productResponse;
    }

    @Override
    public void deleteProduct(long productId) {
        Product product = this.productRepository.findById(productId).orElseThrow(() -> new ResourceNotFoundException("Product" , "productId" , productId));

        this.productRepository.delete(product);
    }

    @Override
    public ProductResponse getProductsBySubCategory(long subCategoryId , Integer pageNumber , Integer pageSize, String sortBy , String sortDir) {

        SubCategory subCategory = this.subCategoryRepository.findById(subCategoryId).orElseThrow(() -> new ResourceNotFoundException("SubCategory" , "subCategoryId" , subCategoryId));;

        Pageable pageable = PageRequest.of(pageNumber , pageSize , (sortDir.equals("asc")) ?  Sort.by(sortBy).ascending() :  Sort.by(sortBy).descending());

        Page<Product> pageProducts = this.productRepository.findBySubCategory(subCategory , pageable);

        List<Product> products = pageProducts.getContent();

        List<ProductDto> productDtoList = products.stream().map(product -> this.productToDto(product)).collect(Collectors.toList());

        ProductResponse productResponse = new ProductResponse();
        productResponse.setProductList(productDtoList);
        productResponse.setPageNumber(pageProducts.getNumber());
        productResponse.setPageSize(pageProducts.getSize());
        productResponse.setTotalElement(pageProducts.getTotalElements());
        productResponse.setTotalPages(pageProducts.getTotalPages());
        productResponse.setLastPage(pageProducts.isLast());

        return productResponse;
    }

    @Override
    public ProductResponse getProductsByCategory(long categoryId , Integer pageNumber , Integer pageSize , String sortBy , String sortDir) {

        Category category = this.categoryRepository.findById(categoryId).orElseThrow(() -> new ResourceNotFoundException("Category" , "categoryId" , categoryId));;

        Pageable pageable = PageRequest.of(pageNumber , pageSize, (sortDir.equals("asc")) ?  Sort.by(sortBy).ascending() :  Sort.by(sortBy).descending());

        Page<Product> pageProducts = this.productRepository.findByCategory(category , pageable);

        List<Product> products = pageProducts.getContent();

        List<ProductDto> productDtoList = products.stream().map(product -> this.productToDto(product)).collect(Collectors.toList());

        ProductResponse productResponse = new ProductResponse();
        productResponse.setProductList(productDtoList);
        productResponse.setPageNumber(pageProducts.getNumber());
        productResponse.setPageSize(pageProducts.getSize());
        productResponse.setTotalElement(pageProducts.getTotalElements());
        productResponse.setTotalPages(pageProducts.getTotalPages());
        productResponse.setLastPage(pageProducts.isLast());

        return productResponse;
    }

    private Product dtoToProduct(ProductDto productDto){
        return this.modelMapper.map(productDto, Product.class);
    }

    private ProductDto productToDto(Product product){
        return this.modelMapper.map(product, ProductDto.class);
    }
}

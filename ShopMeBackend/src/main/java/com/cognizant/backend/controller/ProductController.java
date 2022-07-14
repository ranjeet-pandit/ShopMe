package com.cognizant.backend.controller;

import com.cognizant.backend.config.AppConstants;
import com.cognizant.backend.payload.ApiResponse;
import com.cognizant.backend.payload.ProductDto;
import com.cognizant.backend.payload.ProductResponse;
import com.cognizant.backend.service.FileService;
import com.cognizant.backend.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;
import java.io.InputStream;

@RestController
@RequestMapping("/api")
@CrossOrigin(methods = RequestMethod.GET)
public class ProductController {

    @Autowired
    private ProductService productService;

    @Autowired
    private FileService fileService;

    @Value("${project.image}")
    private String imagePath;

    //post - create Product
    @PostMapping("/category/{categoryId}/sub-category/{subCategoryId}/product")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ProductDto> createProduct(@Valid @RequestBody ProductDto productDto,
                                                    @PathVariable Long categoryId,
                                                    @PathVariable Long subCategoryId){
        ProductDto createProductDto = this.productService.createProduct(productDto , categoryId , subCategoryId);
        return new ResponseEntity<>(createProductDto , HttpStatus.CREATED);
    }


    //put - update Product
    @PutMapping("/product/{productId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ProductDto> updateCategory(@Valid @RequestBody ProductDto productDto , @PathVariable Long productId){
        ProductDto updateProduct = this.productService.updateProduct(productDto , productId);
        return new ResponseEntity<>(updateProduct , HttpStatus.OK);
    }

    //delete - delete Product
    @DeleteMapping("/product/{productId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse> deleteProduct(@PathVariable Long productId){
        this.productService.deleteProduct(productId);
        return new ResponseEntity<>( new ApiResponse("Product Deleted Successfully" , true) , HttpStatus.OK);
    }


    //get - Product get
    @GetMapping("/products")
    public ResponseEntity<ProductResponse> getAllProducts(
            @RequestParam(value = "pageNo" , defaultValue = AppConstants.PAGE_NUMBER, required = false) Integer pageNumber,
            @RequestParam(value = "pageSize" , defaultValue = AppConstants.PAGE_SIZE , required = false) Integer pageSize,
            @RequestParam(value = "sortBy" , defaultValue = AppConstants.SORT_BY , required = false) String sortBy,
            @RequestParam(value = "sortDir" , defaultValue = AppConstants.SORT_DIR , required = false) String sortDir
    ){
        return ResponseEntity.ok(this.productService.getAllProducts(pageNumber , pageSize , sortBy , sortDir));
    }

    @GetMapping("/product/{productId}")
    public ResponseEntity<ProductDto> getProduct(@PathVariable Long productId){
        return ResponseEntity.ok(this.productService.getProductById(productId));
    }

    //get - get products by category
    @GetMapping("/category/{categoryId}/products")
    public ResponseEntity<ProductResponse> getProductsByCategory(
            @PathVariable Long categoryId ,
            @RequestParam(value = "pageNo" , defaultValue =  AppConstants.PAGE_NUMBER , required = false) Integer pageNumber,
            @RequestParam(value = "pageSize" , defaultValue = AppConstants.PAGE_SIZE , required = false) Integer pageSize,
            @RequestParam(value = "sortBy" , defaultValue = AppConstants.SORT_BY , required = false) String sortBy,
            @RequestParam(value = "sortDir" , defaultValue = AppConstants.SORT_DIR , required = false) String sortDir
    ){
        ProductResponse products = this.productService.getProductsByCategory(categoryId , pageNumber , pageSize, sortBy ,sortDir);
        return new ResponseEntity<>(products, HttpStatus.OK);
    }

    //get - get products by category
    @GetMapping("/sub-category/{subCategoryId}/products")
    public ResponseEntity<ProductResponse> getProductsBySubCategory(
            @PathVariable Long subCategoryId,
            @RequestParam(value = "pageNo" , defaultValue =  AppConstants.PAGE_NUMBER , required = false) Integer pageNumber,
            @RequestParam(value = "pageSize" , defaultValue =  AppConstants.PAGE_SIZE , required = false) Integer pageSize,
            @RequestParam(value = "sortBy" , defaultValue = AppConstants.SORT_BY , required = false) String sortBy,
            @RequestParam(value = "sortDir" , defaultValue =  AppConstants.SORT_DIR , required = false) String sortDir
    ){
        ProductResponse products = this.productService.getProductsBySubCategory(subCategoryId , pageNumber , pageSize, sortBy ,sortDir);
        return new ResponseEntity<>(products, HttpStatus.OK);
    }

    //post - Product image upload
    @PostMapping("/product/image/upload/{productId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ProductDto> uploadProductImage(
            @PathVariable Long productId,
            @RequestParam(value = "image")MultipartFile image
            ) throws IOException {


        ProductDto productDto = this.productService.getProductById(productId);

        String fileName = this.fileService.uploadImage(imagePath , image);

        productDto.setImageUrl(fileName);

        ProductDto updatedProduct =  this.productService.updateProduct(productDto , productId);

        return new ResponseEntity<>(updatedProduct , HttpStatus.OK);
    }

    //get - get product image
    @GetMapping(value = "/product/image/{imageUrl}" , produces = MediaType.IMAGE_JPEG_VALUE)
    public void downloadImage(
            @PathVariable("imageUrl") String imageUrl,
            HttpServletResponse response
    ) throws IOException {
        InputStream inputStream = this.fileService.getResource(imagePath , imageUrl);
        response.setContentType(MediaType.IMAGE_JPEG_VALUE);
        StreamUtils.copy(inputStream , response.getOutputStream());
    }
}

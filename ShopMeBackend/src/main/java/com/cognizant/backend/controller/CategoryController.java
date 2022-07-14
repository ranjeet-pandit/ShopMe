package com.cognizant.backend.controller;

import com.cognizant.backend.payload.ApiResponse;
import com.cognizant.backend.payload.CategoryDto;
import com.cognizant.backend.payload.ProductDto;
import com.cognizant.backend.service.CategoryService;
import com.cognizant.backend.service.FileService;
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
import java.util.List;

@RestController
@RequestMapping("/api")
@CrossOrigin(methods = RequestMethod.GET)
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private FileService fileService;

    @Value("${project.image}")
    private String imagePath;

    //post - create Category
    @PostMapping("/category")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<CategoryDto> createCategory(@Valid @RequestBody CategoryDto categoryDto){
        CategoryDto createCategoryDto = this.categoryService.createCategory(categoryDto);
        return new ResponseEntity<>(createCategoryDto , HttpStatus.CREATED);
    }

    //put - update Category
    @PutMapping("/category/{categoryId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<CategoryDto> updateCategory(@Valid @RequestBody CategoryDto categoryDto , @PathVariable Long categoryId){
        CategoryDto updateCategory = this.categoryService.updateCategory(categoryDto , categoryId);
        return new ResponseEntity<>(updateCategory , HttpStatus.OK);
    }

    //delete - delete Category
    @DeleteMapping("/category/{categoryId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse> deleteCategory(@PathVariable Long categoryId){
        this.categoryService.deleteCategory(categoryId);
        return new ResponseEntity<ApiResponse>( new ApiResponse("Category Deleted Successfully" , true) , HttpStatus.OK);
    }


    //get - category get
    @GetMapping("categories/")
    public ResponseEntity<List<CategoryDto>> getAllCategories(){
        return ResponseEntity.ok(this.categoryService.getAllCategories());
    }

    @GetMapping("category/{categoryId}")
    public ResponseEntity<CategoryDto> getCategory(@PathVariable Long categoryId){
        return ResponseEntity.ok(this.categoryService.getCategoryById(categoryId));
    }

    //post - Product image upload
    @PostMapping("/category/image/upload/{categoryId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<CategoryDto> uploadCategoryImage(
            @PathVariable Long categoryId,
            @RequestParam(value = "image") MultipartFile image
    ) throws IOException {

        CategoryDto categoryDto = this.categoryService.getCategoryById(categoryId);

        String fileName = this.fileService.uploadImage(imagePath , image);

        categoryDto.setImageUrl(fileName);

        CategoryDto updatedCategoryDto =  this.categoryService.updateCategory(categoryDto , categoryId);

        return new ResponseEntity<>(updatedCategoryDto , HttpStatus.OK);
    }

    //get - get product image
    @GetMapping(value = "/category/image/{imageUrl}" , produces = MediaType.IMAGE_JPEG_VALUE)
    public void downloadImage(
            @PathVariable("imageUrl") String imageUrl,
            HttpServletResponse response
    ) throws IOException {
        InputStream inputStream = this.fileService.getResource(imagePath , imageUrl);
        response.setContentType(MediaType.IMAGE_JPEG_VALUE);
        StreamUtils.copy(inputStream , response.getOutputStream());
    }

}

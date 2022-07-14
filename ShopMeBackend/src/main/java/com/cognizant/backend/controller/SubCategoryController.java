package com.cognizant.backend.controller;

import com.cognizant.backend.model.SubCategory;
import com.cognizant.backend.payload.ApiResponse;
import com.cognizant.backend.payload.CategoryDto;
import com.cognizant.backend.payload.SubCategoryDto;
import com.cognizant.backend.service.FileService;
import com.cognizant.backend.service.SubCategoryService;
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
public class SubCategoryController {

    @Autowired
    private SubCategoryService subCategoryService;

    @Autowired
    private FileService fileService;

    @Value("${project.image}")
    private String imagePath;


    //post - create subCategory
    @PostMapping("/category/{categoryId}/sub-category")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<SubCategoryDto> createSubCategory(@Valid @RequestBody SubCategoryDto subCategoryDto , @PathVariable Long categoryId){
        SubCategoryDto createSubCategoryDto = this.subCategoryService.createSubCategory(subCategoryDto , categoryId);
        return new ResponseEntity<>(createSubCategoryDto , HttpStatus.CREATED);
    }

    //put - update user
    @PutMapping("/sub-category/{subCategoryId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<SubCategoryDto> updateSubCategory(@Valid @RequestBody SubCategoryDto subCategoryDto , @PathVariable Long subCategoryId){
        SubCategoryDto updateSubCategoryDto = this.subCategoryService.updateSubCategory(subCategoryDto , subCategoryId);
        return new ResponseEntity<>(updateSubCategoryDto , HttpStatus.OK);
    }

    //delete - delete user
    @DeleteMapping("/sub-category/{subCategoryId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse> deleteSubCategory(@PathVariable Long subCategoryId){
        this.subCategoryService.deleteSubCategory(subCategoryId);
        return new ResponseEntity<>( new ApiResponse("SubCategory Deleted Successfully" , true) , HttpStatus.OK);
    }

    //get - user get
    @GetMapping("/sub-categories")
    public ResponseEntity<List<SubCategoryDto>> getAllSubCategories(){
        return ResponseEntity.ok(this.subCategoryService.getAllSubCategories());
    }

    @GetMapping("/sub-category/{subCategoryId}")
    public ResponseEntity<SubCategoryDto> getSubCategory(@PathVariable Long subCategoryId){
        return ResponseEntity.ok(this.subCategoryService.getSubCategoryById(subCategoryId));
    }

    //get - get subCategory by category
    @GetMapping("category/{categoryId}/sub-categories")
    public ResponseEntity<List<SubCategoryDto>> getSubCategoriesByCategory(@PathVariable Long categoryId){
        List<SubCategoryDto> subCategories = this.subCategoryService.getSubCategoryByCategory(categoryId);
        return new ResponseEntity<>(subCategories , HttpStatus.OK);
    }



    //post - Product image upload
    @PostMapping("/sub-category/image/upload/{subCategoryId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<SubCategoryDto> uploadSubCategoryImage(
            @PathVariable Long subCategoryId,
            @RequestParam(value = "image") MultipartFile image
    ) throws IOException {

        SubCategoryDto subcategoryDto = this.subCategoryService.getSubCategoryById(subCategoryId);

        String fileName = this.fileService.uploadImage(imagePath , image);

        subcategoryDto.setImageUrl(fileName);

        SubCategoryDto updatedSubCategoryDto =  this.subCategoryService.updateSubCategory(subcategoryDto , subCategoryId);

        return new ResponseEntity<>(updatedSubCategoryDto , HttpStatus.OK);
    }

    //get - get product image
    @GetMapping(value = "/sub-category/image/{imageUrl}" , produces = MediaType.IMAGE_JPEG_VALUE)
    public void downloadImage(
            @PathVariable("imageUrl") String imageUrl,
            HttpServletResponse response
    ) throws IOException {
        InputStream inputStream = this.fileService.getResource(imagePath , imageUrl);
        response.setContentType(MediaType.IMAGE_JPEG_VALUE);
        StreamUtils.copy(inputStream , response.getOutputStream());
    }
}

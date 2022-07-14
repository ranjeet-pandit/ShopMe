package com.cognizant.backend.payload;

import com.cognizant.backend.model.Category;
import com.cognizant.backend.model.SubCategory;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.Date;

@NoArgsConstructor
@Getter
@Setter
public class ProductDto {

    private long id;

    @NotEmpty(message = "Product's name must not be empty.")
    private String name;

    private String companyName;

    @NotEmpty(message = "Product's description must not be empty.")
    private String description;


    @NotNull(message = "Product's price must not be empty.")
    private double price;

    @NotNull(message = "Product's actual price must not be empty.")
    private double actualPrice;

    @NotEmpty(message = "Product's image url must not be empty.")
    private String imageUrl;

    private Date timestamp;

    private SubCategoryDto subCategory;

    private CategoryDto category;
}

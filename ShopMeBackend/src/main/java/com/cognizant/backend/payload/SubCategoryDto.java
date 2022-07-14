package com.cognizant.backend.payload;

import com.cognizant.backend.model.Category;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;

@NoArgsConstructor
@Getter
@Setter
public class SubCategoryDto {

    private long id;

    @NotEmpty(message = "Title must not be empty.")
    private String title;

    private String imageUrl;

    private CategoryDto category;

}

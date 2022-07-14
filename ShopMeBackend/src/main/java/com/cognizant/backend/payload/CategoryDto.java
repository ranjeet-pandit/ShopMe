package com.cognizant.backend.payload;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.*;

@NoArgsConstructor
@Getter
@Setter
public class CategoryDto {

    private long id;

    @NotEmpty(message = "Category title must not be empty.")
    private String title;

    private String imageUrl;
}

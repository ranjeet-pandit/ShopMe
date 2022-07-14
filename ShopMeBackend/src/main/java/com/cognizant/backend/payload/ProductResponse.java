package com.cognizant.backend.payload;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@NoArgsConstructor
@Getter
@Setter
public class ProductResponse {

    private List<ProductDto> productList;

    private int pageNumber;

    private int pageSize;

    private long totalElement;

    private int totalPages;

    private boolean lastPage;
}

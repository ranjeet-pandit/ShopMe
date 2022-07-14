package com.cognizant.backend.repository;

import com.cognizant.backend.model.Category;
import com.cognizant.backend.model.Product;
import com.cognizant.backend.model.SubCategory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product , Long> {

    Page<Product> findBySubCategory(SubCategory subCategory , Pageable pageable);

    Page<Product> findByCategory(Category category , Pageable pageable);
}

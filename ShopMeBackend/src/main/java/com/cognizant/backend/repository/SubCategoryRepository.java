package com.cognizant.backend.repository;

import com.cognizant.backend.model.Category;
import com.cognizant.backend.model.SubCategory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SubCategoryRepository extends JpaRepository<SubCategory , Long> {

    List<SubCategory> findByCategory(Category category);

}

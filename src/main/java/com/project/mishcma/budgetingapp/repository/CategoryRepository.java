package com.project.mishcma.budgetingapp.repository;

import com.project.mishcma.budgetingapp.entity.Category;
import com.project.mishcma.budgetingapp.entity.CategoryType;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, CategoryType> {
}

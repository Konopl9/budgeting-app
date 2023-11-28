package com.project.mishcma.budgetingapp.repository;

import com.project.mishcma.budgetingapp.entity.Portfolio;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PortfolioRepository extends JpaRepository<Portfolio, String> {

}

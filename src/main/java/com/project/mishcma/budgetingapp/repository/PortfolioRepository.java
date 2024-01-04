package com.project.mishcma.budgetingapp.repository;

import com.project.mishcma.budgetingapp.entity.Portfolio;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;

import java.awt.print.Pageable;
import java.util.List;

public interface PortfolioRepository extends JpaRepository<Portfolio, String> {

	List<Portfolio> findAllByOrderByNameAsc();
}

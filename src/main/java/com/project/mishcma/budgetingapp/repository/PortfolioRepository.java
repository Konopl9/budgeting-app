package com.project.mishcma.budgetingapp.repository;

import com.project.mishcma.budgetingapp.entity.Portfolio;
import jakarta.transaction.Transactional;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface PortfolioRepository extends JpaRepository<Portfolio, String> {

  List<Portfolio> findAllByOrderByNameAsc();

  @Query("update Portfolio portfolio set portfolio.cashBalance = ?1 where portfolio.name = ?2")
  @Modifying
  @Transactional
  void updateCashBalanceForPortfolio(Double cashBalance, String name);
}

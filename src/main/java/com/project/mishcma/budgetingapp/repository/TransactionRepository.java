package com.project.mishcma.budgetingapp.repository;

import com.project.mishcma.budgetingapp.entity.Portfolio;
import com.project.mishcma.budgetingapp.entity.Transaction;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {

	Page<Transaction> findByPortfolioOrderByDateDesc(Portfolio portfolio, Pageable pageable);
}

package com.project.mishcma.budgetingapp.repository;

import com.project.mishcma.budgetingapp.entity.Portfolio;
import com.project.mishcma.budgetingapp.entity.Transaction;
import com.project.mishcma.budgetingapp.entity.TransactionType;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {

  Page<Transaction> findByPortfolioOrderByDateDesc(Portfolio portfolio, Pageable pageable);

  List<Transaction> findByTypeOrderByDateDesc(TransactionType type);
}

package com.project.mishcma.budgetingapp.repository;

import com.project.mishcma.budgetingapp.entity.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {
}

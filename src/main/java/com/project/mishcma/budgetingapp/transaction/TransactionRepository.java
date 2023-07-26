package com.project.mishcma.budgetingapp.transaction;

import org.springframework.data.jpa.repository.JpaRepository;

import java.nio.file.LinkOption;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {
}

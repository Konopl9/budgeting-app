package com.project.mishcma.budgetingapp.service;

import com.project.mishcma.budgetingapp.entity.Transaction;

import java.util.List;

public interface TransactionService {

    List<Transaction> getTransactions();

    Transaction saveTransaction(Transaction transaction);

    Transaction findTransactionById(Long id);

    void deleteTransaction(Long id);
}

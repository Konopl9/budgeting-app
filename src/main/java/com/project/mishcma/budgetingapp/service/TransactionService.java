package com.project.mishcma.budgetingapp.service;

import com.project.mishcma.budgetingapp.entity.Transaction;
import com.project.mishcma.budgetingapp.exception.StockSymbolNotFoundException;

import java.util.List;

public interface TransactionService {

    List<Transaction> getTransactions();

    Transaction saveTransaction(Transaction transaction) throws StockSymbolNotFoundException;

    int saveTransactions(List<Transaction> transactions);

    Transaction findTransactionById(Long id);

    void deleteTransaction(Long id);
}

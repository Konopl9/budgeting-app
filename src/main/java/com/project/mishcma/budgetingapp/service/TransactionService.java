package com.project.mishcma.budgetingapp.service;

import com.project.mishcma.budgetingapp.entity.Portfolio;
import com.project.mishcma.budgetingapp.entity.Transaction;
import com.project.mishcma.budgetingapp.exception.StockSymbolNotFoundException;

import java.util.List;

public interface TransactionService {

    List<Transaction> getTransactions(String portfolioName);

    List<Transaction> getFiveTransactions(Portfolio portfolio);

    Transaction saveTransaction(String portfolioName, Transaction transaction) throws StockSymbolNotFoundException;

    int saveTransactions(String portfolioName, List<Transaction> transactions) throws StockSymbolNotFoundException;

    Transaction findTransactionById(Long id);

    void deleteTransaction(Long id);

    List<Transaction> findAll();

    void deleteAll();
}

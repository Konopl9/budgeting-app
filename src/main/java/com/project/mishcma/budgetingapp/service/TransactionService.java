package com.project.mishcma.budgetingapp.service;

import com.project.mishcma.budgetingapp.dto.PortfolioDTO;
import com.project.mishcma.budgetingapp.dto.TransactionDTO;
import com.project.mishcma.budgetingapp.exception.StockSymbolNotFoundException;
import java.util.List;

public interface TransactionService {

  List<TransactionDTO> getTransactions(String portfolioName);

  List<TransactionDTO> getFiveTransactions(PortfolioDTO portfolioDTO);

  TransactionDTO saveTransaction(String portfolioName, TransactionDTO transaction)
      throws StockSymbolNotFoundException;

  int saveTransactions(String portfolioName, List<TransactionDTO> transactions)
      throws StockSymbolNotFoundException;

  TransactionDTO findTransactionById(Long id);

  void deleteTransaction(Long id);

  List<TransactionDTO> findAll();

  void deleteAll();
}

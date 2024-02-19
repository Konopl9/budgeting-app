package com.project.mishcma.budgetingapp.service;

import com.project.mishcma.budgetingapp.dto.PortfolioDTO;
import com.project.mishcma.budgetingapp.dto.TransactionDTO;
import com.project.mishcma.budgetingapp.entity.Portfolio;
import com.project.mishcma.budgetingapp.entity.Transaction;
import com.project.mishcma.budgetingapp.entity.TransactionType;
import com.project.mishcma.budgetingapp.exception.StockSymbolNotFoundException;
import com.project.mishcma.budgetingapp.mapper.PortfolioMapper;
import com.project.mishcma.budgetingapp.mapper.TransactionMapper;
import com.project.mishcma.budgetingapp.repository.TransactionRepository;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class TransactionServiceImpl implements TransactionService {

  private final Logger logger = LoggerFactory.getLogger(TransactionServiceImpl.class);

  private final TransactionRepository transactionRepository;

  private final PortfolioService portfolioService;
  private final MarketDataService marketDataService;

  public TransactionServiceImpl(
      TransactionRepository transactionRepository,
      PortfolioService portfolioService,
      MarketDataService marketDataService) {
    this.transactionRepository = transactionRepository;
    this.portfolioService = portfolioService;
    this.marketDataService = marketDataService;
  }

  @Override
  public List<TransactionDTO> getTransactions(String portfolioName) {
    Pageable pageable = Pageable.unpaged();
    Page<Transaction> transactionPage =
        transactionRepository.findByPortfolioOrderByPurchaseDateDesc(
            new Portfolio(portfolioName), pageable);

    return transactionPage.getContent().stream()
        .map(TransactionMapper::toDTO)
        .collect(Collectors.toList());
  }

  @Override
  public List<TransactionDTO> getFiveTransactions(PortfolioDTO portfolio) {
    Pageable pageable = PageRequest.of(0, 5);
    Page<Transaction> transactionPage =
        transactionRepository.findByPortfolioOrderByPurchaseDateDesc(
            PortfolioMapper.toEntity(portfolio), pageable);
    return transactionPage.stream().map(TransactionMapper::toDTO).toList();
  }

  @Override
  public TransactionDTO saveTransaction(String portfolioName, TransactionDTO transactionDTOToSave)
      throws StockSymbolNotFoundException {
    Portfolio portfolio = portfolioService.findPortfolioByName(portfolioName);
    Transaction transactionToSave = TransactionMapper.toEntity(transactionDTOToSave);
    transactionToSave.setPortfolio(portfolio);
    String symbol = transactionToSave.getTicker();
    if (marketDataService.getStockSymbolData(symbol).isEmpty()) {
      throw new StockSymbolNotFoundException("Unable to find a stock symbol {} " + symbol);
    }

    if (transactionToSave.getPurchaseDate() == null) {
      transactionToSave.setPurchaseDate(Date.from(Instant.now()));
    }

    double changeInCash;
    if (transactionToSave.getType() == TransactionType.SELL) {
      changeInCash =
          transactionToSave.getQuantity() * transactionToSave.getPrice()
              - transactionToSave.getCommission();
    } else {
      changeInCash =
          -(transactionToSave.getQuantity() * transactionToSave.getPrice()
              + transactionToSave.getCommission());
    }
    transactionToSave.setChangeInCash(changeInCash);

    return TransactionMapper.toDTO(transactionRepository.save(transactionToSave));
  }

  @Override
  public int saveTransactions(String portfolioName, List<TransactionDTO> transactions) {
    List<TransactionDTO> savedTransactions = new ArrayList<>();
    transactions.forEach(
        transaction -> {
          try {
            savedTransactions.add(saveTransaction(portfolioName, transaction));

          } catch (StockSymbolNotFoundException e) {
            logger.error("Unable to save transaction {}", e.getMessage());
          }
        });
    return savedTransactions.size();
  }

  @Override
  public TransactionDTO findTransactionById(Long id) {
    return TransactionMapper.toDTO(unWrapTransaction(transactionRepository.findById(id)));
  }

  @Override
  public void deleteTransaction(Long id) {
    transactionRepository.deleteById(id);
    transactionRepository.findAll().forEach(System.out::println);
  }

  @Override
  public List<TransactionDTO> findAll() {
    return transactionRepository.findAll().stream().map(TransactionMapper::toDTO).toList();
  }

  @Override
  public void deleteAll() {
    transactionRepository.deleteAll();
  }

  private Transaction unWrapTransaction(Optional<Transaction> wrappedTransaction) {
    return wrappedTransaction.orElseThrow(IllegalArgumentException::new);
  }
}

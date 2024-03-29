package com.project.mishcma.budgetingapp.service;

import com.project.mishcma.budgetingapp.dto.PortfolioDTO;
import com.project.mishcma.budgetingapp.dto.TransactionDTO;
import com.project.mishcma.budgetingapp.entity.Portfolio;
import com.project.mishcma.budgetingapp.entity.Transaction;
import com.project.mishcma.budgetingapp.entity.TransactionType;
import com.project.mishcma.budgetingapp.exception.StockSymbolNotFoundException;
import com.project.mishcma.budgetingapp.exception.TransactionSubmissionException;
import com.project.mishcma.budgetingapp.mapper.PortfolioMapper;
import com.project.mishcma.budgetingapp.mapper.TransactionMapper;
import com.project.mishcma.budgetingapp.repository.TransactionRepository;
import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.CacheEvict;
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
  private final PortfolioMapper portfolioMapper;
  private final TransactionMapper transactionMapper;

  public TransactionServiceImpl(
          TransactionRepository transactionRepository,
          PortfolioService portfolioService,
          MarketDataService marketDataService, PortfolioMapper portfolioMapper, TransactionMapper transactionMapper) {
    this.transactionRepository = transactionRepository;
    this.portfolioService = portfolioService;
    this.marketDataService = marketDataService;
    this.portfolioMapper = portfolioMapper;
    this.transactionMapper = transactionMapper;
  }

  @Override
  public List<TransactionDTO> getTransactions(String portfolioName) {
    Pageable pageable = Pageable.unpaged();
    Page<Transaction> transactionPage =
        transactionRepository.findByPortfolioOrderByPurchaseDateDesc(
            new Portfolio(portfolioName), pageable);

    return transactionPage.getContent().stream()
        .map(transactionMapper::toDTO)
        .collect(Collectors.toList());
  }

  @Override
  public List<TransactionDTO> getFiveTransactions(PortfolioDTO portfolio) {
    Pageable pageable = PageRequest.of(0, 5);
    Page<Transaction> transactionPage =
        transactionRepository.findByPortfolioOrderByPurchaseDateDesc(
            portfolioMapper.toEntity(portfolio), pageable);
    return transactionPage.stream().map(transactionMapper::toDTO).toList();
  }

  @CacheEvict(value="positions", allEntries=true)
  @Override
  public TransactionDTO saveTransaction(String portfolioName, TransactionDTO transactionDTOToSave)
      throws StockSymbolNotFoundException {
    Portfolio portfolio = portfolioService.findPortfolioByName(portfolioName);
    Transaction transactionToSave = transactionMapper.toEntity(transactionDTOToSave);
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
          transactionToSave.getInitialQuantity() * transactionToSave.getPrice()
              - transactionToSave.getCommission();

      List<Transaction> transactionsWithThisStock =
          portfolio.getTransactions().stream()
              .filter(transaction -> Objects.equals(transaction.getTicker(), symbol))
              .filter(transaction -> !Objects.equals(transaction.getId(), transactionToSave.getId()))
              .sorted(Comparator.comparing(Transaction::getPurchaseDate))
              .toList();

      int quantity = 0;
      for (Transaction transaction : transactionsWithThisStock) {

        if (transaction.getType() == TransactionType.BUY) {
          quantity += transaction.getInitialQuantity();
        } else {
          quantity -= transaction.getInitialQuantity();
        }
      }

      if (quantity < transactionToSave.getInitialQuantity()) {
        throw new TransactionSubmissionException(
            "Insufficient amount of stocks to sell. Current quantity is "
                + quantity
                + ". "
                + "Trying to sell "
                + transactionToSave.getInitialQuantity());
      }

    } else {
      changeInCash =
          -(transactionToSave.getInitialQuantity() * transactionToSave.getPrice()
              + transactionToSave.getCommission());
    }
    transactionToSave.setChangeInCash(changeInCash);

    return transactionMapper.toDTO(transactionRepository.save(transactionToSave));
  }

  @CacheEvict(value="positions", allEntries=true)
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
    return transactionMapper.toDTO(unWrapTransaction(transactionRepository.findById(id)));
  }

  @CacheEvict(value="positions", allEntries=true)
  @Override
  public void deleteTransaction(Long id) {
    transactionRepository.deleteById(id);
  }

  @Override
  public List<TransactionDTO> findAll() {
    return transactionRepository.findAll().stream().map(transactionMapper::toDTO).toList();
  }

  @Override
  public void deleteAll() {
    transactionRepository.deleteAll();
  }

  private Transaction unWrapTransaction(Optional<Transaction> wrappedTransaction) {
    return wrappedTransaction.orElseThrow(IllegalArgumentException::new);
  }
}

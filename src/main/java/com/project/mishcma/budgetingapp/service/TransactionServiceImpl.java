package com.project.mishcma.budgetingapp.service;

import com.project.mishcma.budgetingapp.entity.Portfolio;
import com.project.mishcma.budgetingapp.entity.Transaction;
import com.project.mishcma.budgetingapp.entity.TransactionType;
import com.project.mishcma.budgetingapp.exception.StockSymbolNotFoundException;
import com.project.mishcma.budgetingapp.repository.TransactionRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class TransactionServiceImpl implements TransactionService {

    private final Logger logger = LoggerFactory.getLogger(TransactionServiceImpl.class);

    private final TransactionRepository transactionRepository;

    private final PortfolioService portfolioService;
    private final MarketDataService marketDataService;

    public TransactionServiceImpl(TransactionRepository transactionRepository, PortfolioService portfolioService, MarketDataService marketDataService) {
        this.transactionRepository = transactionRepository;
        this.portfolioService = portfolioService;
        this.marketDataService = marketDataService;
    }

    @Override
    public List<Transaction> getTransactions(String portfolioName) {
        Pageable pageable = Pageable.unpaged();
    Page<Transaction> transactionPage =
        transactionRepository.findByPortfolioOrderByDateDesc(new Portfolio(portfolioName), pageable);
	    return transactionPage.getContent();
    }

    @Override
    public List<Transaction> getFiveTransactions(Portfolio portfolio) {
        Pageable pageable = PageRequest.of(0, 5);
        Page<Transaction> transactionPage = transactionRepository.findByPortfolioOrderByDateDesc(portfolio, pageable);
        return transactionPage.stream().toList();
    }

    @Override
    public Transaction saveTransaction(String portfolioName, Transaction transactionToSave) throws StockSymbolNotFoundException {
        Portfolio portfolio = portfolioService.findPortfolioByName(portfolioName);
        transactionToSave.setPortfolio(portfolio);
        String symbol = transactionToSave.getTicker();
        if(marketDataService.getStockSymbolData(symbol).isEmpty()) {
            throw new StockSymbolNotFoundException("Unable to find a stock symbol {} " + symbol);
        }

        if (transactionToSave.getDate() == null) {
            transactionToSave.setDate(Date.from(Instant.now()));
        }

        double changeInCash;
        if (transactionToSave.getType() == TransactionType.SELL) {
            changeInCash = transactionToSave.getQuantity() * transactionToSave.getPrice() - transactionToSave.getCommission();
        } else {
            changeInCash = -(transactionToSave.getQuantity() * transactionToSave.getPrice() + transactionToSave.getCommission());
        }
        transactionToSave.setChangeInCash(changeInCash);

        return transactionRepository.save(transactionToSave);
    }

    @Override
    public int saveTransactions(String portfolioName, List<Transaction> transactions) {
        transactions.forEach(transaction -> {
            try {
                saveTransaction(portfolioName, transaction);
            } catch (StockSymbolNotFoundException e) {
                logger.error("Unable to save transaction {}", e.getMessage());
            }
        });
        return transactionRepository.saveAll(transactions).size();
    }

    @Override
    public Transaction findTransactionById(Long id) {
        return unWrapTransaction(transactionRepository.findById(id));
    }

    @Override
    public void deleteTransaction(Long id) {
        transactionRepository.deleteById(id);
        transactionRepository.findAll().forEach(System.out::println);
    }

    @Override
    public List<Transaction> findAll() {
        return transactionRepository.findAll();
    }

    @Override
    public void deleteAll() {
        transactionRepository.deleteAll();
    }

    private Transaction unWrapTransaction(Optional<Transaction> wrappedTransaction) {
        return wrappedTransaction.orElseThrow(IllegalArgumentException::new);
    }
}

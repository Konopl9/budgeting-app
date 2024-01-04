package com.project.mishcma.budgetingapp.service;

import com.project.mishcma.budgetingapp.entity.Portfolio;
import com.project.mishcma.budgetingapp.entity.Transaction;
import com.project.mishcma.budgetingapp.entity.TransactionType;
import com.project.mishcma.budgetingapp.exception.StockSymbolNotFoundException;
import com.project.mishcma.budgetingapp.repository.TransactionRepository;
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
    public Transaction saveTransaction(String portfolioName, Transaction transaction) throws StockSymbolNotFoundException {
        Portfolio portfolio = portfolioService.findPortfolioByName(portfolioName);
        transaction.setPortfolio(portfolio);
        String symbol = transaction.getTicker();
        if(marketDataService.getStockSymbolData(symbol).isEmpty()) {
            throw new StockSymbolNotFoundException("Unable to find a stock symbol {} " + symbol);
        }
        if (transaction.getDate() == null) {
            transaction.setDate(Date.from(Instant.now()));
        }
        double totalAmount;
        if(TransactionType.SELL.equals(transaction.getType())) {
            totalAmount = transaction.getQuantity() * transaction.getPrice() - transaction.getCommission();
        } else {
            totalAmount = transaction.getQuantity() * transaction.getPrice() + transaction.getCommission();
        }
        transaction.setTotalAmount(totalAmount);
        return transactionRepository.save(transaction);
    }

    @Override
    public int saveTransactions(List<Transaction> transactions) {
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

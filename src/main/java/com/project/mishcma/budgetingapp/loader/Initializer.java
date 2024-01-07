package com.project.mishcma.budgetingapp.loader;

import com.project.mishcma.budgetingapp.entity.Portfolio;
import com.project.mishcma.budgetingapp.entity.Transaction;
import com.project.mishcma.budgetingapp.entity.TransactionType;
import com.project.mishcma.budgetingapp.event.TransactionResetEvent;
import com.project.mishcma.budgetingapp.exception.StockSymbolNotFoundException;
import com.project.mishcma.budgetingapp.repository.PortfolioRepository;
import com.project.mishcma.budgetingapp.repository.TransactionRepository;
import com.project.mishcma.budgetingapp.service.FileService;
import com.project.mishcma.budgetingapp.service.PortfolioService;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;

import com.project.mishcma.budgetingapp.service.TransactionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class Initializer {

    private final TransactionService transactionService;
    private final PortfolioRepository portfolioRepository;
    private final PortfolioService portfolioService;
    private final FileService fileService;
    Logger logger = LoggerFactory.getLogger(Initializer.class);

    public Initializer(TransactionService transactionService, FileService fileService, PortfolioRepository portfolioRepository, PortfolioService portfolioService) {
        this.transactionService = transactionService;
        this.portfolioRepository = portfolioRepository;
        this.fileService = fileService;
        this.portfolioService = portfolioService;
    }

    @EventListener({ApplicationReadyEvent.class, TransactionResetEvent.class})
    public void reset() throws StockSymbolNotFoundException {
        logger.info("Pre-populated data:");

        // Create three portfolios
        Portfolio portfolio = new Portfolio("My Portfolio", 1000.0);
        Portfolio dividendPortfolio = new Portfolio("My Dividend Portfolio", 5000.0);
        Portfolio valuePortfolio = new Portfolio("My Value Portfolio", 7500.0);

        portfolioRepository.save(portfolio);
        portfolioRepository.save(dividendPortfolio);
        portfolioRepository.save(valuePortfolio);

        logger.info("Number of transactions: " + transactionService.findAll().size());

        // Check if transactions are already populated in the database
        if (transactionService.findAll().size() != 25) {
            transactionService.deleteAll();

            // Create and save transactions for 7 different stocks in 3 different portfolios
            String[] tickers = {"AAPL", "MSFT", "GOOGL", "AMZN", "NFLX", "TSLA", "V"};
            double[] prices = {150.0, 300.0, 2500.0, 3300.0, 600.0, 800.0, 240.0};

            for (int i = 1; i <= 25; i++) {
                    // Set the date to cover a span of 1 year
                    LocalDateTime localDateTime = LocalDateTime.now().minusMonths(i);
                    Instant instant = localDateTime.atZone(ZoneId.systemDefault()).toInstant();
                    Date transactionDate = Date.from(instant);

                    Transaction transaction = new Transaction(
                            tickers[i % 7],
                            TransactionType.BUY,
                            transactionDate,
                            2d,
                            prices[i % 7],
                            0.35d
                    );

                    transactionService.saveTransaction(portfolio.getName(), transaction);
                }
        }

        // Print all data from the Transaction table
        List<Transaction> allTransactions = transactionService.findAll();
        logger.info("Data from Transaction table:");
        for (Transaction transaction : allTransactions) {
            logger.info(transaction.toString());
        }

        portfolio.setTransactions(allTransactions);
        portfolioService.setCostOfInvestment(portfolio);
        portfolioRepository.save(portfolio);

        // Print all data from S3
        logger.info("S3 Files before upload");
        for (String fileName : fileService.getFileNames()) {
            logger.info("File name: " + fileName);
        }
    }
}

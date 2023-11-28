package com.project.mishcma.budgetingapp.loader;

import com.project.mishcma.budgetingapp.entity.Portfolio;
import com.project.mishcma.budgetingapp.entity.Transaction;
import com.project.mishcma.budgetingapp.entity.TransactionType;
import com.project.mishcma.budgetingapp.event.TransactionResetEvent;
import com.project.mishcma.budgetingapp.repository.PortfolioRepository;
import com.project.mishcma.budgetingapp.repository.TransactionRepository;
import com.project.mishcma.budgetingapp.service.FileService;
import com.project.mishcma.budgetingapp.service.PortfolioService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.Date;
import java.util.List;


@Component
public class Initializer {

    Logger logger = LoggerFactory.getLogger(Initializer.class);
    private final TransactionRepository transactionRepository;

    private final PortfolioRepository portfolioRepository;

    private final PortfolioService portfolioService;

    private final FileService fileService;

    public Initializer(TransactionRepository transactionRepository, FileService fileService, PortfolioRepository portfolioRepository, PortfolioService portfolioService) {
        this.transactionRepository = transactionRepository;
        this.portfolioRepository = portfolioRepository;
        this.fileService = fileService;
        this.portfolioService = portfolioService;
    }

    @EventListener({ApplicationReadyEvent.class, TransactionResetEvent.class})
    public void reset() {
        logger.info("Pre-populated data:");
        Portfolio portfolio = new Portfolio("My Portfolio", 1000.0);
        Portfolio portfolio1 = new Portfolio("My Dividend Portfolio", 1000.0);
        Portfolio portfolio2 = new Portfolio("My Value Portfolio", 1000.0);
        portfolioRepository.save(portfolio);
        portfolioRepository.save(portfolio1);
        portfolioRepository.save(portfolio2);

        logger.info("Number of transactions: " + transactionRepository.findAll().size());

        // Check if transactions are already populated in the database
        if (transactionRepository.count() != 5) {
            transactionRepository.deleteAll();
            // Create and save 5 transactions
            for (int i = 1; i <= 5; i++) {
                Transaction transaction = new Transaction();
                transaction.setTicker("AAPL");
                transaction.setType(TransactionType.BUY);
                transaction.setDate(Date.from(Instant.now()));
                transaction.setQuantity(2d);
                transaction.setPrice(100d);
                transaction.setTotalAmount(200d);
                transaction.setCommission(0.35d);
                transaction.setPortfolio(portfolio);
                transactionRepository.save(transaction);
            }

            for (int i = 1; i <= 5; i++) {
                Transaction transaction = new Transaction();
                transaction.setTicker("MSFT");
                transaction.setType(TransactionType.BUY);
                transaction.setDate(Date.from(Instant.now()));
                transaction.setQuantity(2d);
                transaction.setPrice(1000d);
                transaction.setTotalAmount(2000d);
                transaction.setCommission(0.35d);
                transaction.setPortfolio(portfolio);
                transactionRepository.save(transaction);
            }

            for (int i = 1; i <= 5; i++) {
                Transaction transaction = new Transaction();
                transaction.setTicker("GOOGL");
                transaction.setType(TransactionType.BUY);
                transaction.setDate(Date.from(Instant.now()));
                transaction.setQuantity(10d);
                transaction.setPrice(1000d);
                transaction.setTotalAmount(2000d);
                transaction.setCommission(0.35d);
                transaction.setPortfolio(portfolio);
                transactionRepository.save(transaction);
            }
        }

        // Print all data from the Transaction table
        List<Transaction> allTransactions = transactionRepository.findAll();
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

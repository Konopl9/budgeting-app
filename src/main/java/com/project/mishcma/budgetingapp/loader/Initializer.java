package com.project.mishcma.budgetingapp.loader;

import com.project.mishcma.budgetingapp.entity.Transaction;
import com.project.mishcma.budgetingapp.entity.TransactionType;
import com.project.mishcma.budgetingapp.event.TransactionResetEvent;
import com.project.mishcma.budgetingapp.repository.TransactionRepository;
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

    public Initializer(TransactionRepository transactionRepository) {
        this.transactionRepository = transactionRepository;
    }

    @EventListener({ApplicationReadyEvent.class, TransactionResetEvent.class})
    public void reset() {
        logger.info("Pre-populated data:");
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
                transactionRepository.save(transaction);
            }
        }

        // Print all data from the Transaction table
        List<Transaction> allTransactions = transactionRepository.findAll();
        logger.info("Data from Transaction table:");
        for (Transaction transaction : allTransactions) {
            logger.info(transaction.toString());
        }
    }
}

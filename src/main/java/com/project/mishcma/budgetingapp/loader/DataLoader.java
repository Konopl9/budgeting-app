package com.project.mishcma.budgetingapp.loader;

import com.project.mishcma.budgetingapp.category.Category;
import com.project.mishcma.budgetingapp.category.CategoryRepository;
import com.project.mishcma.budgetingapp.category.CategoryType;
import com.project.mishcma.budgetingapp.transaction.Transaction;
import com.project.mishcma.budgetingapp.transaction.TransactionRepository;
import com.project.mishcma.budgetingapp.transaction.TransactionType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;


@Component
public class DataLoader implements CommandLineRunner {

    Logger logger = LoggerFactory.getLogger(DataLoader.class);

    private final CategoryRepository categoryRepository;
    private final TransactionRepository transactionRepository;

    public DataLoader(CategoryRepository categoryRepository, TransactionRepository transactionRepository) {
        this.categoryRepository = categoryRepository;
        this.transactionRepository = transactionRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        logger.info("Pre-populated data:");
        logger.info("Number of transactions: " + transactionRepository.findAll().size());
        logger.info("Number of categories: " + categoryRepository.findAll().size());
        // Check if categories are already populated in the database
        if (categoryRepository.count() == 0) {
            // Insert the enum values into the Category table
            for (CategoryType categoryType : CategoryType.values()) {
                Category category = new Category();
                category.setName(categoryType);
                categoryRepository.save(category);
            }
        }

        Optional<Category> categoryTest = categoryRepository.findById(1L);

        // Check if transactions are already populated in the database
        if (transactionRepository.count() == 0) {
            // Create and save 5 transactions
            for (int i = 1; i <= 5; i++) {
                Transaction transaction = new Transaction();
                transaction.setAmount(100.0 * i); // Assuming the amount is 100 * i for simplicity
                transaction.setType(TransactionType.INCOME); // Assuming all transactions are of type INCOME for simplicity
                transaction.setCategory(categoryTest.get());
                transactionRepository.save(transaction);
            }
        }

        // Print all data from the Category table
        List<Category> allCategories = categoryRepository.findAll();
        logger.info("Data from Category table:");
        for (Category category : allCategories) {
            logger.info(category.toString());
        }

        // Print all data from the Transaction table
        List<Transaction> allTransactions = transactionRepository.findAll();
        logger.info("Data from Transaction table:");
        for (Transaction transaction : allTransactions) {
            logger.info(transaction.toString());
        }
    }
}

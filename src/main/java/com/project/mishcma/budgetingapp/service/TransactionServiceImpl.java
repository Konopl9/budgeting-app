package com.project.mishcma.budgetingapp.service;

import com.project.mishcma.budgetingapp.entity.Category;
import com.project.mishcma.budgetingapp.entity.Transaction;
import com.project.mishcma.budgetingapp.repository.CategoryRepository;
import com.project.mishcma.budgetingapp.repository.TransactionRepository;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class TransactionServiceImpl implements TransactionService {

    private final TransactionRepository transactionRepository;
    private final CategoryRepository categoryRepository;

    public TransactionServiceImpl(TransactionRepository transactionRepository, CategoryRepository categoryRepository) {
        this.transactionRepository = transactionRepository;
        this.categoryRepository = categoryRepository;
    }

    @Override
    public List<Transaction> getTransactions() {
        return transactionRepository.findAll();
    }

    @Override
    public Transaction saveTransaction(Transaction transaction) {
        if (transaction.getDate() == null) {
            transaction.setDate(Date.from(Instant.now()));
        }

        Category category = categoryRepository.findById(transaction.getCategory().getName()).orElseThrow(IllegalArgumentException::new);
        transaction.setCategory(category);
        return transactionRepository.save(transaction);
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

    private Transaction unWrapTransaction(Optional<Transaction> wrappedTransaction) {
        return wrappedTransaction.orElseThrow(IllegalArgumentException::new);
    }
}

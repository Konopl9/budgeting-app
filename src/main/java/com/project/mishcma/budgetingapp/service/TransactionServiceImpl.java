package com.project.mishcma.budgetingapp.service;

import com.project.mishcma.budgetingapp.entity.Transaction;
import com.project.mishcma.budgetingapp.exception.StockSymbolNotFoundException;
import com.project.mishcma.budgetingapp.repository.TransactionRepository;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class TransactionServiceImpl implements TransactionService {

    private final TransactionRepository transactionRepository;

    private final MarketDataService marketDataService;

    public TransactionServiceImpl(TransactionRepository transactionRepository, MarketDataService marketDataService) {
        this.transactionRepository = transactionRepository;
        this.marketDataService = marketDataService;
    }

    @Override
    public List<Transaction> getTransactions() {
        return transactionRepository.findAll();
    }

    @Override
    public Transaction saveTransaction(Transaction transaction) throws StockSymbolNotFoundException {
        String symbol = transaction.getTicker();
        if(marketDataService.getStockSymbolData(symbol).isEmpty()) {
            throw new StockSymbolNotFoundException("Unable to find a stock symbol {} " + symbol);
        }
        if (transaction.getDate() == null) {
            transaction.setDate(Date.from(Instant.now()));
        }
        transaction.setTotalAmount(transaction.getQuantity()*transaction.getPrice());
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

    private Transaction unWrapTransaction(Optional<Transaction> wrappedTransaction) {
        return wrappedTransaction.orElseThrow(IllegalArgumentException::new);
    }
}

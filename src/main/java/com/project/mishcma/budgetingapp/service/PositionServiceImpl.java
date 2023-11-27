package com.project.mishcma.budgetingapp.service;

import com.project.mishcma.budgetingapp.entity.Portfolio;
import com.project.mishcma.budgetingapp.entity.Position;
import com.project.mishcma.budgetingapp.entity.Transaction;
import com.project.mishcma.budgetingapp.entity.TransactionType;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class PositionServiceImpl implements PositionService {

    private static final double ZERO = 0.0;

    @Override
    public List<Position> createPositionsFromTransactions(Portfolio portfolio) {
        List<Transaction> transactions = portfolio.getTransactions();
        // Sort transactions by date to preserve the order
        transactions.sort(Comparator.comparing(Transaction::getDate));

        Map<String, List<Transaction>> tickerToTransactionMap = new HashMap<>();
        List<Position> calculatedPositions = new ArrayList<>();

        // Populate the map
        for (Transaction transaction : transactions) {
            String ticker = transaction.getTicker();
            List<Transaction> tickerSpecificTransactions = tickerToTransactionMap.computeIfAbsent(ticker, k -> new ArrayList<>());
            tickerSpecificTransactions.add(transaction);
        }

        // Create positions
        for (Map.Entry<String, List<Transaction>> entry : tickerToTransactionMap.entrySet()) {
            Position position = getPosition(entry);
            position.setTransactions(entry.getValue());
            position.setPortfolio(portfolio);
            calculatedPositions.add(position);
        }

        return calculatedPositions;
    }

    private Position getPosition(Map.Entry<String, List<Transaction>> entry) {

        if (entry.getValue() == null) {
            throw new IllegalArgumentException("Transaction list cannot be null");
        }

        double totalQuantity = ZERO;
        double totalBuyCost = ZERO;
        double totalSellCost = ZERO;

        for (Transaction transaction : entry.getValue()) {
            double transactionCost = transaction.getQuantity() * transaction.getPrice() + transaction.getCommission();
            // Calculate quantity and cost
            if (transaction.getType().equals(TransactionType.BUY)) {
                totalQuantity += transaction.getQuantity();
                totalBuyCost += transactionCost;
            } else if (transaction.getType().equals(TransactionType.SELL)) {
                totalQuantity -= transaction.getQuantity();
                totalSellCost += transactionCost;
            }
        }

        // Calculate average price
        double averagePrice;
        if (totalQuantity == 0.0) {
            averagePrice = 0.0;  // Avoid division by zero
        } else if (totalSellCost > totalBuyCost) {
            // Handle cases where total selling cost (including commission) exceeds total buying cost (including commission)
            averagePrice = 0.0;
        } else {
            averagePrice = (totalBuyCost - totalSellCost) / totalQuantity;
        }

        return new Position(entry.getKey(), totalQuantity, averagePrice);
    }
}

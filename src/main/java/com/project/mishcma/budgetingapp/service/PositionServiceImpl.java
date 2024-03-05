package com.project.mishcma.budgetingapp.service;

import com.project.mishcma.budgetingapp.RestDTO.StockDataDTO;
import com.project.mishcma.budgetingapp.dto.PortfolioDTO;
import com.project.mishcma.budgetingapp.dto.TransactionDTO;
import com.project.mishcma.budgetingapp.entity.*;
import com.project.mishcma.budgetingapp.exception.StockDataNotFoundException;
import com.project.mishcma.budgetingapp.model.Position;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class PositionServiceImpl implements PositionService {

    private static final double ZERO = 0.0;
    private final Logger logger = LoggerFactory.getLogger(PositionServiceImpl.class);
    private final MarketDataService marketDataService;

    public PositionServiceImpl(MarketDataService marketDataService) {
        this.marketDataService = marketDataService;
    }

    @Override
    public List<Position> createPositionsFromTransactions(PortfolioDTO portfolioDTO) {
        List<TransactionDTO> transactions = portfolioDTO.getTransactions();
        if (transactions.isEmpty()) {
            return new ArrayList<>();
        }

        // Ensure that transactions is mutable by creating a new ArrayList
        List<TransactionDTO> mutableTransactions = new ArrayList<>(transactions);
        // Sort transactions by date to preserve the order
        mutableTransactions.sort(Comparator.comparing(TransactionDTO::getPurchaseDate));

        Map<String, List<TransactionDTO>> tickerToTransactionMap = new HashMap<>();
        List<Position> calculatedPositions = new ArrayList<>();

        // Populate the map
        for (TransactionDTO transaction : mutableTransactions) {
            String ticker = transaction.getTicker();
            List<TransactionDTO> tickerSpecificTransactions = tickerToTransactionMap.computeIfAbsent(ticker, k -> new ArrayList<>());
            tickerSpecificTransactions.add(transaction);
        }

        try {
            // Fetch stock data from the market data service
            Optional<List<StockDataDTO>> stockDataOptional = marketDataService.postPortfolioData(tickerToTransactionMap.keySet());

            if (stockDataOptional.isPresent()) {
                List<StockDataDTO> stockData = stockDataOptional.get();

                // Create positions
                for (Map.Entry<String, List<TransactionDTO>> entry : tickerToTransactionMap.entrySet()) {
                    String ticker = entry.getKey();
                    List<TransactionDTO> transactionsForTicker = entry.getValue();

                    // Find the StockDataDTO for the current ticker
                    Optional<StockDataDTO> stockDataForTicker = stockData.stream()
                            .filter(stock -> stock.getSymbol().equals(ticker))
                            .findFirst();

                    if (stockDataForTicker.isPresent()) {
                        Position position = getPosition(entry);
                        position.setStockDataDTO(stockDataForTicker.get());
                        position.setCurrentPositionValue(stockDataForTicker.get().getCurrentPrice() * position.getQuantity());
                        position.setTransactions(transactionsForTicker);
                        position.setPortfolio(portfolioDTO);
                        calculatedPositions.add(position);
                    } else {
                        logger.warn("Stock data not available for ticker: " + ticker);
                    }
                }
            } else {
                // Handle the case where stock data is not available
                logger.warn("Stock data not available for any tickers");
            }
        } catch (StockDataNotFoundException e) {
            logger.error("Unable to retrieve stock data: " + e.getMessage());
        }


        return calculatedPositions;
    }

    private Position getPosition(Map.Entry<String, List<TransactionDTO>> entry) {

        if (entry.getValue() == null) {
            throw new IllegalArgumentException("Transaction list cannot be null");
        }

        double totalQuantity = ZERO;
        double totalBuyCost = ZERO;
        double totalSellCost = ZERO;

        for (TransactionDTO transaction : entry.getValue()) {
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

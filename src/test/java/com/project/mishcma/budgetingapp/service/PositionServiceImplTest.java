package com.project.mishcma.budgetingapp.service;

import com.project.mishcma.budgetingapp.entity.Portfolio;
import com.project.mishcma.budgetingapp.entity.Position;
import com.project.mishcma.budgetingapp.entity.Transaction;
import com.project.mishcma.budgetingapp.entity.TransactionType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
class PositionServiceImplTest {

    @Mock
    private MarketDataService marketDataService;

    @InjectMocks
    private PositionServiceImpl positionService;

    @BeforeEach
    public void setUp() {
        this.positionService = new PositionServiceImpl(marketDataService);
    }

    // Not supporting the check from external API
    // Should be decoupled
    // @Test
    void createPositionsFromTransactions() {
        // Arrange
        Portfolio portfolio = new Portfolio();
        List<Transaction> transactionsFromDb = createTransactions();
        portfolio.setTransactions(transactionsFromDb);

        // Act
        List<Position> positions = positionService.createPositionsFromTransactions(portfolio);
        // Sort for consistency
        positions.sort(Comparator.comparing(Position::getTicker));

        // Assert
        assertThat(positions.size()).isEqualTo(3);
        assertThat(positions.get(0).getTicker()).isEqualTo("AAPL");
        assertThat(positions.get(1).getTicker()).isEqualTo("GOOGL");
        assertThat(positions.get(2).getTicker()).isEqualTo("MSFT");

        assertPosition(positions.get(0), 4.0, 117.5);
        assertPosition(positions.get(1), 0.0, 0.0);
        assertPosition(positions.get(2), 5.0, 116.0);
    }

    private static List<Transaction> createTransactions() {
        List<Transaction> transactions = new ArrayList<>();

        Transaction transactionAPPL1 = new Transaction("AAPL", TransactionType.BUY, new Date(), 1.0, 100.0, 5.0);
        Transaction transactionAPPL2 = new Transaction("AAPL", TransactionType.BUY, new Date(), 1.0, 100.0, 5.0);
        Transaction transactionAPPL3 = new Transaction("AAPL", TransactionType.BUY, new Date(), 1.0, 100.0, 5.0);
        Transaction transactionAPPL4 = new Transaction("AAPL", TransactionType.BUY, new Date(), 1.0, 150.0, 5.0);
        transactions.addAll(List.of(transactionAPPL1, transactionAPPL2, transactionAPPL3, transactionAPPL4));

        Transaction transactionGOOGL1 = new Transaction("GOOGL", TransactionType.BUY, new Date(), 1.0, 100.0, 5.0);
        Transaction transactionGOOGL2 = new Transaction("GOOGL", TransactionType.SELL, new Date(), 1.0, 100.0, 4.0);
        Transaction transactionGOOGL3 = new Transaction("GOOGL", TransactionType.BUY, new Date(), 1.0, 100.0, 3.0);
        Transaction transactionGOOGL4 = new Transaction("GOOGL", TransactionType.SELL, new Date(), 1.0, 150.0, 2.0);
        transactions.addAll(List.of(transactionGOOGL1, transactionGOOGL2, transactionGOOGL3, transactionGOOGL4));

        Transaction transactionMSFT1 = new Transaction("MSFT", TransactionType.BUY, new Date(), 1.0, 100.0, 5.0);
        Transaction transactionMSFT2 = new Transaction("MSFT", TransactionType.BUY, new Date(), 1.0, 100.0, 5.0);
        Transaction transactionMSFT3 = new Transaction("MSFT", TransactionType.BUY, new Date(), 2.0, 100.0, 15.0);
        Transaction transactionMSFT4 = new Transaction("MSFT", TransactionType.BUY, new Date(), 1.0, 150.0, 5.0);
        transactions.addAll(List.of(transactionMSFT1, transactionMSFT2, transactionMSFT3, transactionMSFT4));

        return transactions;
    }

    private void assertPosition(Position position, Double expectedQuantity, Double expectedAverageCost) {
        assertThat(position.getQuantity()).isEqualTo(expectedQuantity);
        assertThat(position.getAveragePrice()).isEqualTo(expectedAverageCost);
    }
}
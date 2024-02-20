package com.project.mishcma.budgetingapp.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;

import com.project.mishcma.budgetingapp.dto.PortfolioDTO;
import com.project.mishcma.budgetingapp.dto.TransactionDTO;
import com.project.mishcma.budgetingapp.entity.Transaction;
import com.project.mishcma.budgetingapp.entity.TransactionType;
import com.project.mishcma.budgetingapp.model.Position;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class PositionServiceImplTest {

    @Mock
    private MarketDataService marketDataService;

    @InjectMocks
    private PositionServiceImpl positionService;

    private static List<TransactionDTO> createTransactions() {
        List<TransactionDTO> transactions = new ArrayList<>();

        TransactionDTO transactionAPPL1 = new TransactionDTO("AAPL", TransactionType.BUY, new Date(), 1.0, 100.0, 5.0);
        TransactionDTO transactionAPPL2 = new TransactionDTO("AAPL", TransactionType.BUY, new Date(), 1.0, 100.0, 5.0);
        TransactionDTO transactionAPPL3 = new TransactionDTO("AAPL", TransactionType.BUY, new Date(), 1.0, 100.0, 5.0);
        TransactionDTO transactionAPPL4 = new TransactionDTO("AAPL", TransactionType.BUY, new Date(), 1.0, 150.0, 5.0);
        transactions.addAll(List.of(transactionAPPL1, transactionAPPL2, transactionAPPL3, transactionAPPL4));

        TransactionDTO transactionGOOGL1 = new TransactionDTO("GOOGL", TransactionType.BUY, new Date(), 1.0, 100.0, 5.0);
        TransactionDTO transactionGOOGL2 = new TransactionDTO("GOOGL", TransactionType.SELL, new Date(), 1.0, 100.0, 4.0);
        TransactionDTO transactionGOOGL3 = new TransactionDTO("GOOGL", TransactionType.BUY, new Date(), 1.0, 100.0, 3.0);
        TransactionDTO transactionGOOGL4 = new TransactionDTO("GOOGL", TransactionType.SELL, new Date(), 1.0, 150.0, 2.0);
        transactions.addAll(List.of(transactionGOOGL1, transactionGOOGL2, transactionGOOGL3, transactionGOOGL4));

        TransactionDTO transactionMSFT1 = new TransactionDTO("MSFT", TransactionType.BUY, new Date(), 1.0, 100.0, 5.0);
        TransactionDTO transactionMSFT2 = new TransactionDTO("MSFT", TransactionType.BUY, new Date(), 1.0, 100.0, 5.0);
        TransactionDTO transactionMSFT3 = new TransactionDTO("MSFT", TransactionType.BUY, new Date(), 2.0, 100.0, 15.0);
        TransactionDTO transactionMSFT4 = new TransactionDTO("MSFT", TransactionType.BUY, new Date(), 1.0, 150.0, 5.0);
        transactions.addAll(List.of(transactionMSFT1, transactionMSFT2, transactionMSFT3, transactionMSFT4));

        return transactions;
    }

    @BeforeEach
    public void setUp() {
        this.positionService = new PositionServiceImpl(marketDataService);
    }

    void createPositionsFromTransactions() {
        // Arrange
        PortfolioDTO portfolio = new PortfolioDTO();
        List<TransactionDTO> transactionsFromDb = createTransactions();
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

    private void assertPosition(Position position, Double expectedQuantity, Double expectedAverageCost) {
        assertThat(position.getQuantity()).isEqualTo(expectedQuantity);
        assertThat(position.getAveragePrice()).isEqualTo(expectedAverageCost);
    }
}
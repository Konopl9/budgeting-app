package com.project.mishcma.budgetingapp.mapper;

import static org.junit.jupiter.api.Assertions.*;

import com.project.mishcma.budgetingapp.dto.PortfolioDTO;
import com.project.mishcma.budgetingapp.dto.TransactionDTO;
import com.project.mishcma.budgetingapp.entity.Portfolio;
import com.project.mishcma.budgetingapp.entity.Status;
import com.project.mishcma.budgetingapp.entity.Transaction;
import com.project.mishcma.budgetingapp.entity.TransactionType;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;

class PortfolioMapperTest {

  private PortfolioMapper portfolioMapper;

  @BeforeEach
  void setup() {
    ModelMapper modelMapper = new ModelMapper();
    TransactionMapper transactionMapper = new TransactionMapper(modelMapper);
    portfolioMapper = new PortfolioMapper(modelMapper, transactionMapper);
  }

  @Test
  void testToDTO() {
    // Create a Portfolio entity
    Portfolio portfolio = new Portfolio();
    portfolio.setName("MyPortfolio");
    List<Transaction> transactions = new ArrayList<>();
    Transaction transaction =
        Transaction.builder()
            .id(1L)
            .ticker("AAPL")
            .status(Status.INACTIVE)
            .activeQuantity(1.0d)
            .type(TransactionType.BUY)
            .purchaseDate(Date.from(Instant.now()))
            .initialQuantity(2.0d)
            .price(100.0d)
            .changeInCash(-200.1d)
            .commission(0.1)
            .portfolio(portfolio)
            .build();
    transactions.add(transaction);
    portfolio.setTransactions(transactions);

    PortfolioDTO portfolioDTO = portfolioMapper.toDTO(portfolio);

    assertEquals(portfolio.getName(), portfolioDTO.getName());
    assertEquals(portfolio.getCashBalance(), portfolioDTO.getCashBalance());
    assertEquals(portfolio.getTransactions().size(), portfolioDTO.getTransactions().size());
    assertEquals(
        portfolio.getTransactions().get(0).getId(), portfolioDTO.getTransactions().get(0).getId());
    assertNull(portfolioDTO.getTotalCost());
    assertNull(portfolioDTO.getCostOfInvestments());
    assertNull(portfolioDTO.getNumberOfPositions());
    assertNull(portfolioDTO.getPositions());
  }

  @Test
  public void testToEntity() {
    // Create a Portfolio entity
    PortfolioDTO portfolioDTO = new PortfolioDTO();
    portfolioDTO.setName("MyPortfolio");
    List<TransactionDTO> transactions = new ArrayList<>();
    TransactionDTO transactionDTO =
        TransactionDTO.builder()
            .id(1L)
            .ticker("AAPL")
            .status(Status.INACTIVE)
            .quantity(1.0d)
            .type(TransactionType.BUY)
            .purchaseDate(Date.from(Instant.now()))
            .price(100.0d)
            .changeInCash(-200.1d)
            .commission(0.1)
            .portfolioDTO(portfolioDTO)
            .build();
    transactions.add(transactionDTO);
    portfolioDTO.setTransactions(transactions);

    Portfolio portfolio = portfolioMapper.toEntity(portfolioDTO);

    assertEquals(portfolio.getName(), portfolioDTO.getName());
    assertEquals(portfolio.getCashBalance(), portfolioDTO.getCashBalance());
    assertEquals(portfolio.getTransactions().size(), portfolioDTO.getTransactions().size());
    assertEquals(
            portfolio.getTransactions().get(0).getId(), portfolioDTO.getTransactions().get(0).getId());
    assertNull(portfolioDTO.getTotalCost());
    assertNull(portfolioDTO.getCostOfInvestments());
    assertNull(portfolioDTO.getNumberOfPositions());
    assertNull(portfolioDTO.getPositions());
  }
}

package com.project.mishcma.budgetingapp.mapper;

import static org.junit.jupiter.api.Assertions.*;

import com.project.mishcma.budgetingapp.dto.TransactionDTO;
import com.project.mishcma.budgetingapp.entity.Portfolio;
import com.project.mishcma.budgetingapp.entity.Status;
import com.project.mishcma.budgetingapp.entity.Transaction;
import com.project.mishcma.budgetingapp.entity.TransactionType;
import java.time.Instant;
import java.util.Date;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;

class TransactionMapperTest {

  private TransactionMapper transactionMapper;

  @BeforeEach
  void setup() {
    ModelMapper modelMapper = new ModelMapper();
    transactionMapper = new TransactionMapper(modelMapper);
  }

  @Test
  void testToDTO() {
    Portfolio portfolio = new Portfolio("My Portfolio");
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
    portfolio.setTransactions(List.of(transaction));

    // Perform mapping
    TransactionDTO resultDTO = transactionMapper.toDTO(transaction);

    // Assertions
    assertEquals(transaction.getId(), resultDTO.getId());
    assertEquals(transaction.getTicker(), resultDTO.getTicker());
    assertEquals(transaction.getStatus(), resultDTO.getStatus());
    assertEquals(transaction.getInitialQuantity(), resultDTO.getQuantity());
    assertEquals(transaction.getType(), resultDTO.getType());
    assertEquals(transaction.getPurchaseDate(), resultDTO.getPurchaseDate());
    assertEquals(transaction.getPrice(), resultDTO.getPrice());
    assertEquals(transaction.getChangeInCash(), resultDTO.getChangeInCash());
    assertEquals(transaction.getCommission(), resultDTO.getCommission());
  }

  @Test
  void testToEntity() {
    // Initial Transaction
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
            .build();

    // Perform mapping
    Transaction resultDTO = transactionMapper.toEntity(transactionDTO);

    // Assertions
    assertEquals(transactionDTO.getId(), resultDTO.getId());
    assertEquals(transactionDTO.getTicker(), resultDTO.getTicker());
    assertEquals(transactionDTO.getStatus(), resultDTO.getStatus());
    assertEquals(transactionDTO.getQuantity(), resultDTO.getInitialQuantity());
    assertEquals(transactionDTO.getType(), resultDTO.getType());
    assertEquals(transactionDTO.getPurchaseDate(), resultDTO.getPurchaseDate());
    assertEquals(transactionDTO.getPrice(), resultDTO.getPrice());
    assertEquals(transactionDTO.getChangeInCash(), resultDTO.getChangeInCash());
    assertEquals(transactionDTO.getCommission(), resultDTO.getCommission());
  }
}

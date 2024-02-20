package com.project.mishcma.budgetingapp.model;

import com.project.mishcma.budgetingapp.RestDTO.StockDataDTO;
import com.project.mishcma.budgetingapp.dto.PortfolioDTO;
import com.project.mishcma.budgetingapp.dto.TransactionDTO;

import java.io.Serializable;
import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class Position implements Serializable {

  private String ticker;

  private StockDataDTO stockDataDTO;

  private Double quantity;

  private Double averagePrice;

  private Double currentPositionValue;

  private PortfolioDTO portfolio;

  private Double percentOfPortfolio;

  private List<TransactionDTO> transactions;

  public Position(String ticker, Double quantity, Double averagePrice) {
    this.ticker = ticker;
    this.quantity = quantity;
    this.averagePrice = averagePrice;
  }

  public Position(String cash, Double cashBalance) {
    this.ticker = cash;
    this.currentPositionValue = cashBalance;
  }
}

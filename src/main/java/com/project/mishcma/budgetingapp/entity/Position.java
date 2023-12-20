package com.project.mishcma.budgetingapp.entity;

import com.project.mishcma.budgetingapp.DTO.StockDataDTO;
import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class Position implements Cloneable {

  private String ticker;

  private StockDataDTO stockDataDTO;

  private Double quantity;

  private Double averagePrice;

  private Double currentPositionValue;

  private Portfolio portfolio;

  private Double percentOfPortfolio;

  private List<Transaction> transactions;

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

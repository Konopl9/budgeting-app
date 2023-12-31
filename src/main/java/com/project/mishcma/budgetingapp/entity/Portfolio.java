package com.project.mishcma.budgetingapp.entity;

import jakarta.persistence.*;
import java.util.List;

import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Portfolio {

  @Id private String name;

  @PositiveOrZero
  private Double cashBalance;

  private Double costOfInvestments;

  private Double totalCost;

  private Short numberOfPositions;

  @OneToMany(mappedBy = "portfolio", cascade = CascadeType.ALL)
  private List<Transaction> transactions;

  @Transient private List<Position> positions;

  public Portfolio(String name) {
    this.name = name;
  }

  public Portfolio(String name, Double cashBalance) {
    this.name = name;
    this.cashBalance = cashBalance;
  }
}

package com.project.mishcma.budgetingapp.entity;

import jakarta.persistence.*;
import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Portfolio {

  @Id private String name;

  private Double cashBalance;

  private Double costOfInvestments;

  private Double totalCost;

  private Short numberOfPositions;

  @OneToMany(mappedBy = "portfolio", cascade = CascadeType.ALL)
  private List<Transaction> transactions;

  @Transient private List<Position> positions;

  public Portfolio(String name, Double cashBalance) {
    this.name = name;
    this.cashBalance = cashBalance;
  }
}

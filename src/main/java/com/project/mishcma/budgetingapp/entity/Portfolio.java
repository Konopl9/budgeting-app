package com.project.mishcma.budgetingapp.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.PositiveOrZero;
import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "portfolios")
public class Portfolio {

  @Id private String name;

  private Double cashBalance;

  @OneToMany(mappedBy = "portfolio", cascade = CascadeType.ALL)
  private List<Transaction> transactions;

  public Portfolio(String name) {
    this.name = name;
  }

  public Portfolio(String name, Double cashBalance) {
    this.name = name;
    this.cashBalance = cashBalance;
  }
}

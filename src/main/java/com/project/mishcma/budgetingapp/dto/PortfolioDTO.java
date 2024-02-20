package com.project.mishcma.budgetingapp.dto;

import com.project.mishcma.budgetingapp.model.Position;
import jakarta.validation.constraints.PositiveOrZero;

import java.io.Serializable;
import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class PortfolioDTO implements Serializable {

  private String name;

  @PositiveOrZero private Double cashBalance;

  private Double costOfInvestments;

  private Double totalCost;

  private Short numberOfPositions;

  private List<TransactionDTO> transactions;

  private List<Position> positions;

  public PortfolioDTO(String name) {
    this.name = name;
  }

  @Override
  public String toString() {
    return "PortfolioDTO{"
        + "name='"
        + name
        + '\''
        + ", cashBalance="
        + cashBalance
        + ", costOfInvestments="
        + costOfInvestments
        + ", totalCost="
        + totalCost
        + ", numberOfPositions="
        + numberOfPositions
        + ", transactions="
        + transactions
        + ", positions="
        + positions
        + '}';
  }
}

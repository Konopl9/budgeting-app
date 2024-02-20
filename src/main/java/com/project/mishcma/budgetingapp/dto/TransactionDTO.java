package com.project.mishcma.budgetingapp.dto;

import com.project.mishcma.budgetingapp.entity.TransactionType;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;

import java.io.Serializable;
import java.util.Date;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

@Getter
@Setter
@NoArgsConstructor
public class TransactionDTO implements Serializable {

  private Long id;

  @NotEmpty private String ticker;

  private TransactionType type;

  @PastOrPresent
  @DateTimeFormat(pattern = "yyyy-MM-dd")
  private Date purchaseDate;

  @Positive private Double quantity;

  @Positive private Double price;

  private Double changeInCash;

  @PositiveOrZero private Double commission;

  private Double unrealizedGainOrLoss;

  public TransactionDTO(
      String ticker,
      TransactionType type,
      Date purchaseDate,
      Double quantity,
      Double price,
      Double commission) {
    this.ticker = ticker;
    this.type = type;
    this.purchaseDate = purchaseDate;
    this.quantity = quantity;
    this.price = price;
    this.commission = commission;
    this.changeInCash = quantity * price - commission;
  }

  @Override
  public String toString() {
    return "TransactionDTO{"
        + "id="
        + id
        + ", ticker='"
        + ticker
        + '\''
        + ", type="
        + type
        + ", purchaseDate="
        + purchaseDate
        + ", quantity="
        + quantity
        + ", price="
        + price
        + ", changeInCash="
        + changeInCash
        + ", commission="
        + commission
        + ", unrealizedGainOrLoss="
        + unrealizedGainOrLoss
        + '}';
  }
}

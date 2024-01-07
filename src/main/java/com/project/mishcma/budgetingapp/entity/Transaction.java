package com.project.mishcma.budgetingapp.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import java.util.Date;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Transaction {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private String ticker;

  @Enumerated(EnumType.STRING)
  private TransactionType type;

  @PastOrPresent
  @DateTimeFormat(pattern = "yyyy-MM-dd")
  private Date date;

  @Positive private Double quantity;

  @Positive private Double price;

  private Double changeInCash;

  @PositiveOrZero private Double commission;

  @ManyToOne
  @JoinColumn(name = "portfolio_name")
  private Portfolio portfolio;

  public Transaction(
      String ticker,
      TransactionType type,
      Date date,
      Double quantity,
      Double price,
      Double commission) {
    this.ticker = ticker;
    this.type = type;
    this.date = date;
    this.quantity = quantity;
    this.price = price;
    this.commission = commission;
    this.changeInCash = quantity * price - commission;
  }

  @Override
  public String toString() {
    return "Transaction{"
        + "ticker='"
        + ticker
        + '\''
        + ", type="
        + type
        + ", date="
        + date
        + ", quantity="
        + quantity
        + ", price="
        + price
        + ", changeInCash="
        + changeInCash
        + ", commission="
        + commission
        + '}';
  }
}

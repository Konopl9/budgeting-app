package com.project.mishcma.budgetingapp.entity;

import jakarta.persistence.*;
import java.util.Date;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "transactions")
public class Transaction {

  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "transaction_id_seq")
  @SequenceGenerator(name = "transaction_id_seq", sequenceName = "transaction_id_seq", allocationSize = 1)
  private Long id;

  private String ticker;

  @Enumerated(EnumType.STRING)
  private TransactionType type;

  private Date purchaseDate;

  private Double quantity;

  private Double price;

  private Double changeInCash;

  private Double commission;

  @ManyToOne
  @JoinColumn(name = "portfolio_name")
  private Portfolio portfolio;

  public Transaction(
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
    return "Transaction{"
        + "ticker='"
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
        + '}';
  }
}

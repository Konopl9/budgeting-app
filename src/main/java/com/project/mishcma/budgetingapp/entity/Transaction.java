package com.project.mishcma.budgetingapp.entity;

import jakarta.persistence.*;
import java.util.Date;

import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "transactions")
public class Transaction {

  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "transaction_id_seq")
  @SequenceGenerator(name = "transaction_id_seq", sequenceName = "transaction_id_seq", allocationSize = 1)
  private Long id;

  private String ticker;

  @Enumerated(EnumType.STRING)
  private Status status;

  private Double activeQuantity;

  @Enumerated(EnumType.STRING)
  private TransactionType type;

  private Date purchaseDate;

  private Double initialQuantity;

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
      Double initialQuantity,
      Double price,
      Double commission) {
    this.ticker = ticker;
    this.type = type;
    this.purchaseDate = purchaseDate;
    this.initialQuantity = initialQuantity;
    this.price = price;
    this.commission = commission;
    this.changeInCash = initialQuantity * price - commission;
  }

  @Override
  public String toString() {
    return "Transaction{" +
            "id=" + id +
            ", ticker='" + ticker + '\'' +
            ", status=" + status +
            ", activeQuantity=" + activeQuantity +
            ", type=" + type +
            ", purchaseDate=" + purchaseDate +
            ", initialQuantity=" + initialQuantity +
            ", price=" + price +
            ", changeInCash=" + changeInCash +
            ", commission=" + commission +
            ", portfolio=" + portfolio +
            '}';
  }
}

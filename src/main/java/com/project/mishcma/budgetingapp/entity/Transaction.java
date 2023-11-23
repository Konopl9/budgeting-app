package com.project.mishcma.budgetingapp.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@Entity
@Table
@Getter
@Setter
@ToString
public class Transaction {

    public Transaction() {}

    public Transaction(String ticker, TransactionType type, Date date, Double quantity, Double price, Double commission) {
        this.ticker = ticker;
        this.type = type;
        this.date = date;
        this.quantity = quantity;
        this.price = price;
        this.commission = commission;
        this.totalAmount = quantity * price - commission;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String ticker;

    @Enumerated(EnumType.STRING)
    private TransactionType type;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date date;

    private Double quantity;

    private Double price;

    private Double totalAmount;

    private Double commission;



}

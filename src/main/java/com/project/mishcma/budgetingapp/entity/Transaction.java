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

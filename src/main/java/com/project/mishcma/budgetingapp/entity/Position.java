package com.project.mishcma.budgetingapp.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class Position {

    private String ticker;

    private Double quantity;

    private Double averagePrice;

    private Portfolio portfolio;

    private List<Transaction> transactions;

    public Position(String ticker, Double quantity, Double averagePrice) {
        this.ticker = ticker;
        this.quantity = quantity;
        this.averagePrice = averagePrice;
    }
}

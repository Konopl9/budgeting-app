package com.project.mishcma.budgetingapp.entity;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Portfolio {

    @Id
    private String name;

    private Double cashBalance;

    private Double costOfInvestments;

    @OneToMany(mappedBy = "portfolio", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Transaction> transactions;

    @Transient
    private List<Position> positions;

    public Portfolio(String name, Double cashBalance) {
        this.name = name;
        this.cashBalance = cashBalance;
    }
}

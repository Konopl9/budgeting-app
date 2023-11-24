package com.project.mishcma.budgetingapp.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Entity
@Getter
@Setter
public class Position {

    @Id
    private String symbol;

    private Integer quantity;
    private Double averagePrice;

    @ManyToOne
    @JoinColumn(name = "portfolio_name", nullable = false)
    private Portfolio portfolio;

    @OneToMany(mappedBy = "position", cascade = CascadeType.ALL)
    private List<Transaction> transactions;

}

package com.project.mishcma.budgetingapp.entity;


import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Entity
@Getter
@Setter
public class Portfolio {

    @Id
    private String name;
    private Double cashBalance;

    @OneToMany(mappedBy = "portfolio", cascade = CascadeType.ALL)
    private List<Position> positions;

    private Double costOfInvestments;


}

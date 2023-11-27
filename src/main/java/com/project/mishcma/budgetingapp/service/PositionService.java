package com.project.mishcma.budgetingapp.service;

import com.project.mishcma.budgetingapp.entity.Portfolio;
import com.project.mishcma.budgetingapp.entity.Position;

import java.util.List;

public interface PositionService {
    List<Position> createPositionsFromTransactions(Portfolio portfolio);

}

package com.project.mishcma.budgetingapp.service;

import com.project.mishcma.budgetingapp.dto.PortfolioDTO;
import com.project.mishcma.budgetingapp.entity.Portfolio;
import com.project.mishcma.budgetingapp.model.Position;

import java.util.List;

public interface PositionService {
    List<Position> createPositionsFromTransactions(PortfolioDTO portfolioDTO);

}

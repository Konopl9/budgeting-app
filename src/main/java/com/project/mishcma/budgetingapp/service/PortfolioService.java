package com.project.mishcma.budgetingapp.service;

import com.project.mishcma.budgetingapp.entity.Portfolio;
import com.project.mishcma.budgetingapp.entity.Position;

import java.util.List;

public interface PortfolioService {

    List<String> getPortfoliosNames();

    Portfolio findPortfolioByName(String name);

    Portfolio generatePortfolioPositionsByName(String name);

    void deletePortfolioById(String name);

    void setCostOfInvestment(Portfolio portfolio);

    void setTotalCost(Portfolio portfolio);
}

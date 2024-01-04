package com.project.mishcma.budgetingapp.service;

import com.project.mishcma.budgetingapp.entity.Portfolio;
import java.util.List;
import java.util.Map;

public interface PortfolioService {

  List<String> getPortfoliosNames();

  Portfolio findPortfolioByName(String name);

  Portfolio generatePortfolioPositionsByName(String name);

  void deletePortfolioById(String name);

  void setCostOfInvestment(Portfolio portfolio);

  void setTotalCost(Portfolio portfolio);

  Map<String, Double> getPortfolioAllocation(Portfolio portfolio);

  boolean isPortfolioNameExists(String portfolioName);

  Portfolio save(Portfolio portfolio);
}

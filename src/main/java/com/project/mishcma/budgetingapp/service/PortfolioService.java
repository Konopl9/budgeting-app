package com.project.mishcma.budgetingapp.service;

import com.project.mishcma.budgetingapp.dto.PortfolioDTO;
import com.project.mishcma.budgetingapp.entity.Portfolio;
import java.util.List;
import java.util.Map;

public interface PortfolioService {

  List<String> getPortfoliosNames();

  Portfolio findPortfolioByName(String name);

  PortfolioDTO generatePortfolioPositionsByName(String name);

  void deletePortfolioById(String name);

  Map<String, Double> getPortfolioAllocation(PortfolioDTO portfolioDTO);

  boolean isPortfolioNameExists(String portfolioName);

  PortfolioDTO save(PortfolioDTO portfolioDTO);

  void updateCashBalance(PortfolioDTO portfolioDTO);
}

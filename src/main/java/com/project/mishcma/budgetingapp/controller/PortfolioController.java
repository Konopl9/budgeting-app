package com.project.mishcma.budgetingapp.controller;

import com.project.mishcma.budgetingapp.entity.Portfolio;
import com.project.mishcma.budgetingapp.service.PortfolioService;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("portfolios")
public class PortfolioController {

  private final PortfolioService portfolioService;

  public PortfolioController(PortfolioService portfolioService) {
    this.portfolioService = portfolioService;
  }

  @GetMapping
  public String showAllForm(Model model) {
    model.addAttribute("portfolioNames", portfolioService.getPortfoliosNames());
    Portfolio portfolio = portfolioService.generatePortfolioPositionsByName("My Portfolio");
    model.addAttribute("portfolio", portfolio);
    Map<String, Double> allocationMap = portfolioService.getPortfolioAllocation(portfolio);
    List<String> allocationLabels = new ArrayList<>(allocationMap.keySet());
    List<Double> allocationData = new ArrayList<>(allocationMap.values());
    model.addAttribute("allocationLabels", allocationLabels);
    model.addAttribute("allocationData", allocationData);
    return "portfolios";
  }

  @GetMapping(value = "/{name}/positions")
  public String showPortfolioPositions(@PathVariable String name, Model model) {
    Portfolio portfolio = portfolioService.generatePortfolioPositionsByName(name);
    model.addAttribute("portfolio", portfolio);
    return "portfolios";
  }
}

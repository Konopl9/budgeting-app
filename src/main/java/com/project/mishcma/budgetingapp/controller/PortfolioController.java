package com.project.mishcma.budgetingapp.controller;

import com.project.mishcma.budgetingapp.entity.Portfolio;
import com.project.mishcma.budgetingapp.entity.Transaction;
import com.project.mishcma.budgetingapp.service.PortfolioService;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.project.mishcma.budgetingapp.service.TransactionService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("portfolios")
public class PortfolioController {

  private final PortfolioService portfolioService;

  private final TransactionService transactionService;

  public PortfolioController(PortfolioService portfolioService, TransactionService transactionService) {
    this.portfolioService = portfolioService;
    this.transactionService = transactionService;
  }

  @GetMapping
  public String showAllForm(Model model) {
    List<String> portfolioName = portfolioService.getPortfoliosNames();
    model.addAttribute("portfolioNames", portfolioName);
    model.addAttribute("selectedPortfolio", portfolioName.get(0));
    Portfolio portfolio = portfolioService.generatePortfolioPositionsByName(portfolioName.get(0));
    model.addAttribute("portfolio", portfolio);
    Map<String, Double> allocationMap = portfolioService.getPortfolioAllocation(portfolio);
    List<String> allocationLabels = new ArrayList<>(allocationMap.keySet());
    List<Double> allocationData = new ArrayList<>(allocationMap.values());
    model.addAttribute("allocationLabels", allocationLabels);
    model.addAttribute("allocationData", allocationData);
    List<Transaction> transactions = transactionService.getFiveTransactions(portfolio);
    model.addAttribute("transactions", transactions);
    return "portfolios";
  }

  @GetMapping(value = "/{name}")
  public String showPortfolioPositions(@PathVariable String name, Model model) {
    List<String> portfolioName = portfolioService.getPortfoliosNames();
    model.addAttribute("portfolioNames", portfolioName);
    Portfolio portfolio = portfolioService.generatePortfolioPositionsByName(name);
    model.addAttribute("selectedPortfolio", name);
    model.addAttribute("portfolio", portfolio);
    Map<String, Double> allocationMap = portfolioService.getPortfolioAllocation(portfolio);
    List<String> allocationLabels = new ArrayList<>(allocationMap.keySet());
    List<Double> allocationData = new ArrayList<>(allocationMap.values());
    model.addAttribute("allocationLabels", allocationLabels);
    model.addAttribute("allocationData", allocationData);
    List<Transaction> transactions = transactionService.getFiveTransactions(portfolio);
    model.addAttribute("transactions", transactions);
    return "portfolios";
  }
}

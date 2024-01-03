package com.project.mishcma.budgetingapp.controller;

import com.project.mishcma.budgetingapp.entity.Portfolio;
import com.project.mishcma.budgetingapp.entity.Transaction;
import com.project.mishcma.budgetingapp.service.PortfolioService;
import com.project.mishcma.budgetingapp.service.TransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("portfolios")
@RequiredArgsConstructor
public class PortfolioController {

  private final PortfolioService portfolioService;
  private final TransactionService transactionService;

  @GetMapping
  public String showAllForm() {
    return redirectToFirstPortfolio();
  }

  @GetMapping("/{name}")
  public String showPortfolioPositions(@PathVariable String name, ModelMap model) {
    return showPortfolioPage(name, model);
  }

  @PostMapping("/add")
  public String addPortfolio(@ModelAttribute Portfolio newPortfolio, ModelMap model) {
    return addOrUpdatePortfolio(newPortfolio, model);
  }

  @GetMapping("/delete/{name}")
  public String deletePortfolio(@PathVariable String name) {
    portfolioService.deletePortfolioById(name);
    return redirectToFirstPortfolio();
  }

  private String redirectToFirstPortfolio() {
    List<String> portfolioName = portfolioService.getPortfoliosNames();
    return "redirect:/portfolios/" + portfolioName.get(0);
  }

  private String showPortfolioPage(String name, ModelMap model) {
    List<String> portfolioName = portfolioService.getPortfoliosNames();
    model.addAttribute("portfolioNames", portfolioName);
    model.addAttribute("selectedPortfolio", name);
    model.addAttribute("newPortfolio", new Portfolio());
    Portfolio portfolio = portfolioService.generatePortfolioPositionsByName(name);
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

  private String addOrUpdatePortfolio(Portfolio newPortfolio, ModelMap model) {
    if (portfolioService.isPortfolioNameExists(newPortfolio.getName())) {
      model.addAttribute("error", "Portfolio name already exists");
      return showPortfolioPage(portfolioService.getPortfoliosNames().get(0), model);
    }

    Portfolio addedPortfolio = portfolioService.save(newPortfolio);
    return "redirect:/portfolios/" + addedPortfolio.getName();
  }
}
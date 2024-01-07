package com.project.mishcma.budgetingapp.controller;

import com.project.mishcma.budgetingapp.entity.Portfolio;
import com.project.mishcma.budgetingapp.entity.Transaction;
import com.project.mishcma.budgetingapp.service.PortfolioService;
import com.project.mishcma.budgetingapp.service.TransactionService;
import jakarta.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

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
  public String showPortfolioPositions(@PathVariable String name, Model model) {
    return showPortfolioPage(name, model);
  }

  @PostMapping("/add")
  public String addPortfolio(@ModelAttribute Portfolio newPortfolio, Model model) {
    if (portfolioService.isPortfolioNameExists(newPortfolio.getName())) {
      model.addAttribute("error", "Portfolio name already exists");
      return showPortfolioPage(portfolioService.getPortfoliosNames().get(0), model);
    }

    Portfolio addedPortfolio = portfolioService.save(newPortfolio);
    return "redirect:/portfolios/" + addedPortfolio.getName();
  }

  @PostMapping("/updateCash")
  private String updateCash(
      @Valid @ModelAttribute Portfolio newPortfolio, BindingResult bindingResult, Model model) {
    if (bindingResult.hasErrors()) {
      model.addAttribute("error", "Unable to update cash position with negative value");
      return showPortfolioPage(newPortfolio.getName(), model);
    }

    Portfolio portfolioToUpdate = portfolioService.findPortfolioByName(newPortfolio.getName());
    portfolioToUpdate.setCashBalance(newPortfolio.getCashBalance());
    portfolioService.save(portfolioToUpdate);
    return "redirect:/portfolios/" + portfolioToUpdate.getName();
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

  private String showPortfolioPage(String name, Model model) {
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
}
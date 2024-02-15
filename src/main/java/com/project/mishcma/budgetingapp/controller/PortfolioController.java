package com.project.mishcma.budgetingapp.controller;

import com.project.mishcma.budgetingapp.dto.PortfolioDTO;
import com.project.mishcma.budgetingapp.entity.Portfolio;
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
  public String showAllForm(@ModelAttribute("newPortfolio") Portfolio newPortfolio, Model model) {
    return redirectToFirstPortfolioOrEmpty(newPortfolio, model);
  }

  @GetMapping("/{name}")
  public String showPortfolioPositions(@PathVariable String name, Model model) {
    return showPortfolioPage(name, model);
  }

  @PostMapping("/add")
  public String addPortfolio(
      @ModelAttribute("newPortfolio") @Valid PortfolioDTO newPortfolio,
      BindingResult bindingResult,
      Model model) {
    if (bindingResult.hasErrors()) {
      model.addAttribute("error", "Unable to add portfolio. Please check your inputs.");
      return showPortfolioPage(newPortfolio.getName(), model);
    }

    if (portfolioService.isPortfolioNameExists(newPortfolio.getName())) {
      model.addAttribute("error", "Portfolio name already exists");
      return showPortfolioPage(portfolioService.getPortfoliosNames().get(0), model);
    }

    PortfolioDTO addedPortfolio = portfolioService.save(newPortfolio);
    return "redirect:/portfolios/" + addedPortfolio.getName();
  }

  @PostMapping("/updateCash")
  public String updateCash(
      @Valid @ModelAttribute("newPortfolio") PortfolioDTO newPortfolio,
      BindingResult bindingResult,
      Model model) {
    if (bindingResult.hasErrors()) {
      model.addAttribute("error", "Unable to update cash position with negative value");
      return showPortfolioPage(newPortfolio.getName(), model);
    }

    portfolioService.updateCashBalance(newPortfolio);
    Portfolio updatedPortfolio = portfolioService.findPortfolioByName(newPortfolio.getName());
    return "redirect:/portfolios/" + updatedPortfolio.getName();
  }

  @GetMapping("/delete/{name}")
  public String deletePortfolio(@PathVariable String name) {
    portfolioService.deletePortfolioById(name);
    return "redirect:/portfolios";
  }

  private String redirectToFirstPortfolioOrEmpty(Portfolio newPortfolio, Model model) {
    List<String> portfolioNames = portfolioService.getPortfoliosNames();
    if (portfolioNames.isEmpty()) {
      model.addAttribute("newPortfolio", newPortfolio);
      return "empty-portfolio";
    }
    return "redirect:/portfolios/" + portfolioNames.get(0);
  }

  private String showPortfolioPage(String name, Model model) {
    List<String> portfolioName = portfolioService.getPortfoliosNames();
    model.addAttribute("portfolioNames", portfolioName);
    model.addAttribute("portfolioName", name);
    model.addAttribute("newPortfolio", new Portfolio());
    PortfolioDTO portfolio = portfolioService.generatePortfolioPositionsByName(name);
    model.addAttribute("portfolio", portfolio);
    Map<String, Double> allocationMap = portfolioService.getPortfolioAllocation(portfolio);
    model.addAttribute("allocationLabels", new ArrayList<>(allocationMap.keySet()));
    model.addAttribute("allocationData", new ArrayList<>(allocationMap.values()));
    model.addAttribute("transactions", transactionService.getFiveTransactions(portfolio));
    return "portfolios";
  }
}

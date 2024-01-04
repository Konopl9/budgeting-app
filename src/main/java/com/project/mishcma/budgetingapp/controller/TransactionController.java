package com.project.mishcma.budgetingapp.controller;

import com.project.mishcma.budgetingapp.entity.Portfolio;
import com.project.mishcma.budgetingapp.entity.Transaction;
import com.project.mishcma.budgetingapp.event.TransactionResetEvent;
import com.project.mishcma.budgetingapp.exception.StockSymbolNotFoundException;
import com.project.mishcma.budgetingapp.service.PortfolioService;
import com.project.mishcma.budgetingapp.service.TransactionService;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;

@Controller
@RequestMapping("transactions")
public class TransactionController {

  private final TransactionService transactionService;
  private final ApplicationEventPublisher publisher;

  public TransactionController(
          TransactionService transactionService, ApplicationEventPublisher publisher) {
    this.transactionService = transactionService;
    this.publisher = publisher;
  }

  @GetMapping( "/showAll")
  public String showAllForm(@RequestParam(name = "portfolioName") String portfolioName, Model model) {
    model.addAttribute("transactions", transactionService.getTransactions(portfolioName));
    model.addAttribute("selectedPortfolio", portfolioName);
    return "transactions";
  }

  @GetMapping( "/showUpdateForm/{id}")
  public ModelAndView showUpdateForm(@PathVariable Long id) {
    ModelAndView mav = new ModelAndView("update-transaction");
    Transaction transaction = transactionService.findTransactionById(id);
    mav.addObject("transaction", transaction);
    return mav;
  }

  @PostMapping( "/createTransaction")
  public RedirectView createTransaction(
      @ModelAttribute Transaction transaction, @RequestParam(name = "portfolioName") String portfolioName, RedirectAttributes attributes) {
    RedirectView rv = new RedirectView("/transactions/showAll", true);
    try {
      transactionService.saveTransaction(portfolioName, transaction);
      attributes.addFlashAttribute("success", "Successfully added!");
      attributes.addAttribute("portfolioName", portfolioName);
      return rv;
    } catch (StockSymbolNotFoundException e) {
      attributes.addFlashAttribute("error", e.getMessage());
      attributes.addAttribute("portfolioName", portfolioName);
      return rv;
    }
  }

  @PostMapping("/reset")
  public String resetTransactions(@RequestParam(name = "portfolioName") String portfolioName, Model model) {
    publisher.publishEvent(new TransactionResetEvent());
    model.addAttribute("transactions", transactionService.getTransactions(portfolioName));
    return "transactions :: transactions-list";
  }

  @PostMapping("/updateTransaction")
  public String updateTransaction(String portfolioName, Transaction transaction) {
    try {
      transactionService.saveTransaction(portfolioName, transaction);
    } catch (StockSymbolNotFoundException e) {
      throw new RuntimeException(e);
    }
    return "redirect:/transactions/showAll";
  }

  @ResponseBody
  @DeleteMapping(value = "/{id}", produces = MediaType.TEXT_HTML_VALUE)
  public String deleteTransaction(@PathVariable Long id) {
    transactionService.deleteTransaction(id);
    return "";
  }
}

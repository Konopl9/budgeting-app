package com.project.mishcma.budgetingapp.controller;

import com.project.mishcma.budgetingapp.entity.Transaction;
import com.project.mishcma.budgetingapp.event.TransactionResetEvent;
import com.project.mishcma.budgetingapp.exception.StockSymbolNotFoundException;
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
  public String showAllForm(Model model) {
    model.addAttribute("transactions", transactionService.getTransactions());
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
      @ModelAttribute Transaction transaction, RedirectAttributes attributes) {
    RedirectView rv = new RedirectView("/transactions/showAll", true);
    try {
      transactionService.saveTransaction(transaction);
      attributes.addFlashAttribute("success", "Successfully added!");
      return rv;
    } catch (StockSymbolNotFoundException e) {
      attributes.addFlashAttribute("error", e.getMessage());
      return rv;
    }
  }

  @PostMapping("/reset")
  public String resetTransactions(Model model) {
    publisher.publishEvent(new TransactionResetEvent());
    model.addAttribute("transactions", transactionService.getTransactions());
    return "transactions :: transactions-list";
  }

  @PostMapping("/updateTransaction")
  public String updateTransaction(Transaction transaction) {
    try {
      transactionService.saveTransaction(transaction);
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

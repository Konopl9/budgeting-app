package com.project.mishcma.budgetingapp.controller;

import com.project.mishcma.budgetingapp.dto.TransactionDTO;
import com.project.mishcma.budgetingapp.event.TransactionResetEvent;
import com.project.mishcma.budgetingapp.exception.StockSymbolNotFoundException;
import com.project.mishcma.budgetingapp.service.TransactionService;
import jakarta.validation.Valid;
import java.util.Objects;
import java.util.stream.Collectors;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
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

  @GetMapping("/showAll")
  public String showAllForm(
      @RequestParam(name = "portfolioName") String portfolioName, Model model) {
    model.addAttribute("transactions", transactionService.getTransactions(portfolioName));
    model.addAttribute("portfolioName", portfolioName);
    return "transactions";
  }

  @GetMapping("/showUpdateForm/{id}")
  public ModelAndView showUpdateForm(@PathVariable Long id, @RequestParam String portfolioName) {
    ModelAndView mav = new ModelAndView("update-transaction");
    TransactionDTO transaction = transactionService.findTransactionById(id);
    mav.addObject("transaction", transaction);
    mav.addObject("portfolioName", portfolioName);
    return mav;
  }

  @PostMapping("/createTransaction")
  public RedirectView createTransaction(
      @Valid @ModelAttribute TransactionDTO transaction,
      BindingResult bindingResult,
      @RequestParam(name = "portfolioName") String portfolioName,
      RedirectAttributes attributes) {
    RedirectView rv = new RedirectView("/transactions/showAll", true);
    if (bindingResult.hasErrors()) {
      attributes.addFlashAttribute(
          "error",
          bindingResult.getAllErrors().stream()
              .filter(Objects::nonNull)
              .map(
                  error -> {
                    if (error instanceof FieldError) {
                      return "Field '"
                          + ((FieldError) error).getField()
                          + "': "
                          + error.getDefaultMessage()
                          + "<br>";
                    } else {
                      return "Object '"
                          + error.getObjectName()
                          + "': "
                          + error.getDefaultMessage()
                          + "<br>";
                    }
                  })
              .collect(Collectors.joining("<br>")));
      attributes.addAttribute("portfolioName", portfolioName);
      return rv;
    }

    try {
      transactionService.saveTransaction(portfolioName, transaction);
    } catch (StockSymbolNotFoundException e) {
      attributes.addFlashAttribute(
          "error", "Unable to update stock symbol " + transaction.getTicker());
      attributes.addAttribute("portfolioName", portfolioName);
      return rv;
    }
    attributes.addFlashAttribute("success", "Successfully added!");
    attributes.addAttribute("portfolioName", portfolioName);
    return rv;
  }

  @PostMapping("/reset")
  public String resetTransactions(
      @RequestParam(name = "portfolioName") String portfolioName, Model model) {
    publisher.publishEvent(new TransactionResetEvent());
    model.addAttribute("transactions", transactionService.getTransactions(portfolioName));
    return "transactions :: transactions-list";
  }

  @PostMapping("/updateTransaction")
  public String updateTransaction(
      @RequestParam String portfolioName,
      @Valid TransactionDTO transaction,
      BindingResult bindingResult,
      Model model,
      RedirectAttributes redirectAttributes) {
    if (bindingResult.hasErrors()) {
      model.addAttribute(
          "error",
          bindingResult.getAllErrors().stream()
              .filter(Objects::nonNull)
              .map(
                  error -> {
                    if (error instanceof FieldError) {
                      return "Field '"
                          + ((FieldError) error).getField()
                          + "': "
                          + error.getDefaultMessage()
                          + "<br>";
                    } else {
                      return "Object '"
                          + error.getObjectName()
                          + "': "
                          + error.getDefaultMessage()
                          + "<br>";
                    }
                  })
              .collect(Collectors.joining("<br>")));
      model.addAttribute("transaction", transaction);
      model.addAttribute("portfolioName", portfolioName);
      return "update-transaction";
    }
    try {
      transactionService.saveTransaction(portfolioName, transaction);
    } catch (StockSymbolNotFoundException e) {
      model.addAttribute("error", "Unable to update stock symbol " + transaction.getTicker());
      model.addAttribute("transaction", transaction);
      model.addAttribute("portfolioName", portfolioName);
      return "update-transaction";
    }
    redirectAttributes.addAttribute("portfolioName", portfolioName);
    return "redirect:/transactions/showAll";
  }

  @ResponseBody
  @DeleteMapping(value = "/{id}", produces = MediaType.TEXT_HTML_VALUE)
  public String deleteTransaction(@PathVariable Long id) {
    transactionService.deleteTransaction(id);
    return "";
  }
}

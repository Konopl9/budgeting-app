package com.project.mishcma.budgetingapp.controller;


import com.project.mishcma.budgetingapp.entity.Transaction;
import com.project.mishcma.budgetingapp.event.TransactionResetEvent;
import com.project.mishcma.budgetingapp.service.TransactionService;
import io.github.wimdeblauwe.hsbt.mvc.HtmxResponse;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class TransactionController {

    private final TransactionService transactionService;
    private final ApplicationEventPublisher publisher;

    public TransactionController(TransactionService transactionService, ApplicationEventPublisher publisher) {
        this.transactionService = transactionService;
        this.publisher = publisher;
    }

    @GetMapping(value = "/showAll")
    public String showAllForm(Model model) {
        model.addAttribute("transactions", transactionService.getTransactions());
        return "transactions";
    }

    @GetMapping(value = "/showUpdateForm/{id}")
    public ModelAndView showUpdateForm(@PathVariable Long id) {
        ModelAndView mav = new ModelAndView("update-transaction");
        Transaction transaction = transactionService.findTransactionById(id);
        mav.addObject("transaction", transaction);
        return mav;
    }

    @PostMapping(value = "/createTransaction")
    public HtmxResponse createTransaction(@ModelAttribute Transaction transaction, Model model) {
        transactionService.saveTransaction(transaction);
        model.addAttribute("transactions", transactionService.getTransactions());
        return new HtmxResponse()
                .addTemplate("transactions :: transactions-list")
                .addTrigger("clear-form");
    }

    @PostMapping("/reset")
    public String resetTransactions(Model model) {
        publisher.publishEvent(new TransactionResetEvent());
        model.addAttribute("transactions", transactionService.getTransactions());
        return "transactions :: transactions-list";
    }

    @PostMapping( "/updateTransaction")
    public String updateTransaction(Transaction transaction) {
        transactionService.saveTransaction(transaction);
        return "redirect:/showAll";
    }

    @ResponseBody
    @DeleteMapping(value = "/{id}", produces = MediaType.TEXT_HTML_VALUE)
    public String deleteTransaction(@PathVariable Long id) {
        transactionService.deleteTransaction(id);
        return "";
    }

}

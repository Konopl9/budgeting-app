package com.project.mishcma.budgetingapp.controller;


import com.project.mishcma.budgetingapp.entity.Transaction;
import com.project.mishcma.budgetingapp.event.TransactionResetEvent;
import com.project.mishcma.budgetingapp.service.TransactionService;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

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

    @GetMapping(value = "/showCreateForm")
    public String showCreateForm() {
        return "create-transaction";
    }

    @GetMapping(value = "/showUpdateForm/{id}")
    public String showUpdateForm(@PathVariable Long id, Model model) {
        Transaction selectedTransaction = transactionService.findTransactionById(id);
        model.addAttribute("transaction", selectedTransaction);
        return "update-transaction";
    }

    @PostMapping(value = "/createTransaction")
    public String createTransaction(Transaction transaction, Model model) {
        transactionService.saveTransaction(transaction);
        return "redirect:/showAllTransactions";
    }

    @PostMapping("/reset")
    public String resetTransactions(Model model) {
        publisher.publishEvent(new TransactionResetEvent());
        model.addAttribute("transactions", transactionService.getTransactions());
        return "transactions :: transactions-list";
    }

    @PostMapping(value = "/updateTransaction/{id}")
    public String updateTransaction(@PathVariable Long id, Transaction transaction, Model model) {
        transactionService.saveTransaction(transaction);
        return "redirect:/showAllTransactions";
    }

    @ResponseBody
    @DeleteMapping(value = "/{id}", produces = MediaType.TEXT_HTML_VALUE)
    public String deleteTransaction(@PathVariable Long id) {
        transactionService.deleteTransaction(id);
        return "";
    }

}

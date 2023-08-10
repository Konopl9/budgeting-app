package com.project.mishcma.budgetingapp.controller;


import com.project.mishcma.budgetingapp.entity.Transaction;
import com.project.mishcma.budgetingapp.service.TransactionService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class TransactionController {

    private final TransactionService transactionService;

    public TransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;
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

    @PostMapping(value = "/updateTransaction/{id}")
    public String updateTransaction(@PathVariable Long id, Transaction transaction, Model model) {
        transactionService.saveTransaction(transaction);
        return "redirect:/showAllTransactions";
    }

    @GetMapping(value = "/deleteTransaction/{id}")
    public String deleteTransaction(@PathVariable Long id) {
        transactionService.deleteTransaction(id);
        return "redirect:/showAllTransactions";
    }


}

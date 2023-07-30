package com.project.mishcma.budgetingapp.controller;


import com.project.mishcma.budgetingapp.service.TransactionService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/transaction")
public class TransactionController {

    private final TransactionService transactionService;

    public TransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @GetMapping("/all")
    public String getAllTransaction(Model model) {
        model.addAttribute("transactions", transactionService.getTransactions());
        return "transaction";
    }


}

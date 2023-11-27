package com.project.mishcma.budgetingapp.controller;

import com.project.mishcma.budgetingapp.service.PortfolioService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("portfolios")
public class PortfolioController {

    private final PortfolioService portfolioService;

    public PortfolioController(PortfolioService portfolioService) {
        this.portfolioService = portfolioService;
    }

    @GetMapping
    public String showAllForm(Model model) {
        model.addAttribute("portfolioNames", portfolioService.getPortfoliosNames());
        return "portfolios";
    }
}

package com.project.mishcma.budgetingapp.service;

import com.project.mishcma.budgetingapp.entity.Portfolio;
import com.project.mishcma.budgetingapp.entity.Position;
import com.project.mishcma.budgetingapp.entity.Transaction;
import com.project.mishcma.budgetingapp.repository.PortfolioRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PortfolioServiceImpl implements PortfolioService {

    private final PortfolioRepository portfolioRepository;

    private final PositionService positionService;

    public PortfolioServiceImpl(PortfolioRepository portfolioRepository, PositionService positionService) {
        this.portfolioRepository = portfolioRepository;
        this.positionService = positionService;
    }

    @Override
    public List<String> getPortfoliosNames() {
        return portfolioRepository.findAll().stream().map(Portfolio::getName).toList();
    }

    @Override
    public Portfolio findPortfolioByName(String name) {
        Portfolio portfolio = portfolioRepository.findById(name).orElseThrow(EntityNotFoundException::new);

        List<Position> createdPositions = positionService.createPositionsFromTransactions(portfolio);
        portfolio.setPositions(createdPositions);

        return portfolio;
    }

    @Override
    public void deletePortfolioById(String name) {
        portfolioRepository.deleteById(name);
    }

    @Override
    public void setCostOfInvestment(Portfolio portfolio) {
        List<Transaction> transactions = portfolio.getTransactions();
        double costOfInvestment = 0.0;

        if (!transactions.isEmpty()) {
            costOfInvestment = transactions.stream().mapToDouble(transaction -> transaction.getQuantity() * transaction.getPrice()).sum();
        }

        portfolio.setCostOfInvestments(costOfInvestment);
    }
}

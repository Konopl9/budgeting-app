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
        return portfolioRepository.findById(name).orElseThrow(EntityNotFoundException::new);
    }

    @Override
    public Portfolio generatePortfolioPositionsByName(String name) {
        Portfolio portfolio = findPortfolioByName(name);
        List<Position> positions = positionService.createPositionsFromTransactions(portfolio);
        portfolio.setPositions(positions);
        setCostOfInvestment(portfolio);
        setTotalCost(portfolio);
        setNumberOfPositions(portfolio);
        portfolio.getPositions().forEach(position -> setPercentOfThePortfolio(position, portfolio));
        return portfolio;
    }

    private void setNumberOfPositions(Portfolio portfolio) {
        portfolio.setNumberOfPositions((short) portfolio.getPositions().size());
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

    @Override
    public void setTotalCost(Portfolio portfolio) {
        List<Position> positions = portfolio.getPositions();
        double totalCosts = 0.0;

        if (!positions.isEmpty()) {
            totalCosts = positions.stream().mapToDouble(position -> position.getQuantity() * position.getStockDataDTO().getCurrentPrice()).sum();
            totalCosts += portfolio.getCashBalance();
        }

        portfolio.setTotalCost(totalCosts);
    }

    public static void setPercentOfThePortfolio(Position position, Portfolio portfolio) {
        if(position != null && portfolio.getTotalCost() != null) {
            position.setPercentOfPortfolio((position.getStockDataDTO().getCurrentPrice() * position.getQuantity() / portfolio.getTotalCost()) * 100.0);
        }
    }

}

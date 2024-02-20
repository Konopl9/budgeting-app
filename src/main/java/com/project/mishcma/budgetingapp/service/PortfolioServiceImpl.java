package com.project.mishcma.budgetingapp.service;

import com.project.mishcma.budgetingapp.dto.PortfolioDTO;
import com.project.mishcma.budgetingapp.dto.TransactionDTO;
import com.project.mishcma.budgetingapp.entity.Portfolio;
import com.project.mishcma.budgetingapp.mapper.PortfolioMapper;
import com.project.mishcma.budgetingapp.model.Position;
import com.project.mishcma.budgetingapp.repository.PortfolioRepository;
import jakarta.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

@Service
public class PortfolioServiceImpl implements PortfolioService {

  private static final double DEFAULT_CASH_BALANCE = 0.0;

  private final PortfolioRepository portfolioRepository;

  private final PositionService positionService;

  public PortfolioServiceImpl(
      PortfolioRepository portfolioRepository, PositionService positionService) {
    this.portfolioRepository = portfolioRepository;
    this.positionService = positionService;
  }

  private static void setNumberOfPositions(PortfolioDTO portfolio) {
    portfolio.setNumberOfPositions((short) portfolio.getPositions().size());
  }

  private static void setCostOfInvestment(PortfolioDTO portfolio) {
    List<TransactionDTO> transactions = portfolio.getTransactions();
    double costOfInvestment = 0.0;

    if (!transactions.isEmpty()) {
      costOfInvestment =
          transactions.stream()
              .mapToDouble(transaction -> transaction.getQuantity() * transaction.getPrice())
              .sum();
    }

    portfolio.setCostOfInvestments(costOfInvestment);
  }

  private static void setTotalCost(PortfolioDTO portfolio) {
    List<Position> positions = portfolio.getPositions();
    double totalCosts = 0.0;

    if (!positions.isEmpty()) {
      totalCosts =
          positions.stream()
              .mapToDouble(
                  position -> position.getQuantity() * position.getStockDataDTO().getCurrentPrice())
              .sum();
    }

    totalCosts += portfolio.getCashBalance();
    portfolio.setTotalCost(totalCosts);
  }

  private static void setPercentOfThePortfolio(Position position, PortfolioDTO portfolio) {
    if (position != null && portfolio.getTotalCost() != null) {
      position.setPercentOfPortfolio(
          (position.getStockDataDTO().getCurrentPrice()
                  * position.getQuantity()
                  / portfolio.getTotalCost())
              * 100.0);
    }
  }

  @Override
  public List<String> getPortfoliosNames() {
    return portfolioRepository.findAllByOrderByNameAsc().stream().map(Portfolio::getName).toList();
  }

  @Override
  public Portfolio findPortfolioByName(String name) {
    return portfolioRepository.findById(name).orElseThrow(EntityNotFoundException::new);
  }

  @Override
  @Cacheable(cacheNames = "positions", key = "#name")
  public PortfolioDTO generatePortfolioPositionsByName(String name) {
    System.out.println("Building it from scratch.....");
    PortfolioDTO portfolio = PortfolioMapper.toDTO(findPortfolioByName(name));
    List<Position> positions = positionService.createPositionsFromTransactions(portfolio);
    // List<Position> positions = new ArrayList<>();
    portfolio.setPositions(positions);
    setCostOfInvestment(portfolio);
    setTotalCost(portfolio);
    setNumberOfPositions(portfolio);
    portfolio.getPositions().forEach(position -> setPercentOfThePortfolio(position, portfolio));
    return portfolio;
  }

  public Map<String, Double> getPortfolioAllocation(PortfolioDTO portfolio) {
    // Clone the list of positions to avoid modifying the original object
    List<Position> positions = new ArrayList<>(portfolio.getPositions());

    // Include cash as a separate position
    Position cashPosition = new Position("Cash", portfolio.getCashBalance());
    positions.add(cashPosition);

    // Calculate total value of all positions
    double totalValue =
        positions.stream()
            .mapToDouble(
                position ->
                    position.getCurrentPositionValue() != null
                        ? position.getCurrentPositionValue()
                        : 0.0)
            .sum();

    // Calculate allocation percentages
    return positions.stream()
        .collect(
            Collectors.toMap(
                Position::getTicker,
                position ->
                    position.getCurrentPositionValue() != null
                        ? position.getCurrentPositionValue() / totalValue * 100
                        : 0.0));
  }

  @Override
  public boolean isPortfolioNameExists(String portfolioName) {
    return portfolioRepository.findById(portfolioName).isPresent();
  }

  @Override
  @CacheEvict(value="positions", allEntries=true)
  public void updateCashBalance(PortfolioDTO portfolioDTO) {
    Portfolio portfolioToUpdate = findPortfolioByName(portfolioDTO.getName());
    portfolioRepository.updateCashBalanceForPortfolio(
        portfolioDTO.getCashBalance(), portfolioToUpdate.getName());
  }

  @Override
  @CacheEvict(value="positions", allEntries=true)
  public PortfolioDTO save(PortfolioDTO portfolioDTO) {
    Portfolio portfolio = PortfolioMapper.toEntity(portfolioDTO);

    if (portfolio.getCashBalance() == null) {
      portfolio.setCashBalance(DEFAULT_CASH_BALANCE);
    }

    return PortfolioMapper.toDTO(portfolioRepository.save(portfolio));
  }

  @Override
  @CacheEvict(value="positions", allEntries=true)
  public void deletePortfolioById(String name) {
    portfolioRepository.deleteById(name);
  }
}

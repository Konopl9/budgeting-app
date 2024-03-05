package com.project.mishcma.budgetingapp.mapper;

import com.project.mishcma.budgetingapp.dto.PortfolioDTO;
import com.project.mishcma.budgetingapp.dto.TransactionDTO;
import com.project.mishcma.budgetingapp.entity.Portfolio;
import com.project.mishcma.budgetingapp.entity.Transaction;
import java.util.List;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeMap;
import org.springframework.stereotype.Component;

@Component
public class PortfolioMapper {

  private final ModelMapper modelMapper;

  private final TransactionMapper transactionMapper;

  public PortfolioMapper(ModelMapper modelMapper, TransactionMapper transactionMapper) {
    this.modelMapper = modelMapper;
    this.transactionMapper = transactionMapper;

    TypeMap<Portfolio, PortfolioDTO> transactionTypeMapToDTO =
        modelMapper.createTypeMap(Portfolio.class, PortfolioDTO.class);
    transactionTypeMapToDTO.addMappings(mapper -> mapper.skip(PortfolioDTO::setTransactions));

    TypeMap<PortfolioDTO, Portfolio> transactionTypeMapToEntity =
        modelMapper.createTypeMap(PortfolioDTO.class, Portfolio.class);
    transactionTypeMapToEntity.addMappings(mapper -> mapper.skip(Portfolio::setTransactions));
  }

  public PortfolioDTO toDTO(Portfolio portfolioToMap) {
    PortfolioDTO portfolioDTO = modelMapper.map(portfolioToMap, PortfolioDTO.class);
    List<Transaction> transactions = portfolioToMap.getTransactions();
    if (transactions != null) {
      portfolioDTO.setTransactions(transactions.stream().map(transactionMapper::toDTO).toList());
    }

    return portfolioDTO;
  }

  public Portfolio toEntity(PortfolioDTO portfolioDTO) {
    Portfolio portfolio = modelMapper.map(portfolioDTO, Portfolio.class);
    List<TransactionDTO> transactions = portfolioDTO.getTransactions();
    if (transactions != null) {
      portfolio.setTransactions(transactions.stream().map(transactionMapper::toEntity).toList());
    }
    return portfolio;
  }
}

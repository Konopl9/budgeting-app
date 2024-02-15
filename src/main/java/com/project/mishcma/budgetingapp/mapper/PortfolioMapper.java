package com.project.mishcma.budgetingapp.mapper;

import com.project.mishcma.budgetingapp.dto.PortfolioDTO;
import com.project.mishcma.budgetingapp.entity.Portfolio;
import org.modelmapper.ModelMapper;

public class PortfolioMapper {

  private static final ModelMapper modelMapper = new ModelMapper();

  public static PortfolioDTO toDTO(Portfolio portfolio) {
    return modelMapper.map(portfolio, PortfolioDTO.class);
  }

  public static Portfolio toEntity(PortfolioDTO portfolioDTO) {
    return modelMapper.map(portfolioDTO, Portfolio.class);
  }
}

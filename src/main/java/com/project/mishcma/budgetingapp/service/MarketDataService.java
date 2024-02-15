package com.project.mishcma.budgetingapp.service;

import com.project.mishcma.budgetingapp.RestDTO.QuoteDTO;
import com.project.mishcma.budgetingapp.RestDTO.StockDataDTO;
import com.project.mishcma.budgetingapp.RestDTO.SymbolDTO;
import com.project.mishcma.budgetingapp.exception.StockDataNotFoundException;
import com.project.mishcma.budgetingapp.exception.StockSymbolNotFoundException;
import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface MarketDataService {
  Optional<SymbolDTO> getStockSymbolData(String symbol) throws StockSymbolNotFoundException;

  Optional<List<SymbolDTO>> postStockSymbolsData(List<String> symbols)
      throws StockSymbolNotFoundException;

  Optional<QuoteDTO> getStockQuoteData(String symbol);

  Optional<List<StockDataDTO>> postPortfolioData(Set<String> symbols)
      throws StockDataNotFoundException;
}

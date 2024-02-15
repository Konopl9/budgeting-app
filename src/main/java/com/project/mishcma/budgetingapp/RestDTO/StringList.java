package com.project.mishcma.budgetingapp.RestDTO;

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class StringList {

  private List<String> symbols;

  public StringList(List<String> symbols) {
    this.symbols = symbols;
  }

	public void setSymbols(List<String> symbols) {
    this.symbols = symbols != null ? new ArrayList<>(symbols) : new ArrayList<>();
  }

  public void addSymbol(String symbol) {
    if (symbol != null) {
      symbols.add(symbol);
    }
  }

  public boolean containsSymbol(String symbol) {
    return symbols.contains(symbol);
  }
}

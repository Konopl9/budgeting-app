package com.project.mishcma.budgetingapp.RestDTO;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record SymbolDTO(String symbol, String description, String displaySymbol, String type) {}

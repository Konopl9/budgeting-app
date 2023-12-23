package com.project.mishcma.budgetingapp.DTO;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record QuoteDTO(String symbol, Double currentPrice, Double openingPrice, long timestamp) {}

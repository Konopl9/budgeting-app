package com.project.mishcma.budgetingapp.RestDTO;

import java.io.Serializable;

public class StockDataDTO implements Serializable {
	private String symbol;
	private Double currentPrice;
	private Double openingPrice;
	private String name;
	private String logo;
	private String industry;

	public StockDataDTO() {
	}

	public StockDataDTO(String symbol, Double currentPrice, Double openingPrice, String name, String logo, String industry) {
		this.symbol = symbol;
		this.currentPrice = currentPrice;
		this.openingPrice = openingPrice;
		this.name = name;
		this.logo = logo;
		this.industry = industry;
	}

	public String getSymbol() {
		return symbol;
	}

	public void setSymbol(String symbol) {
		this.symbol = symbol;
	}

	public Double getCurrentPrice() {
		return currentPrice;
	}

	public void setCurrentPrice(Double currentPrice) {
		this.currentPrice = currentPrice;
	}

	public Double getOpeningPrice() {
		return openingPrice;
	}

	public void setOpeningPrice(Double openingPrice) {
		this.openingPrice = openingPrice;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getLogo() {
		return logo;
	}

	public void setLogo(String logo) {
		this.logo = logo;
	}

	public String getIndustry() {
		return industry;
	}

	public void setIndustry(String industry) {
		this.industry = industry;
	}
}

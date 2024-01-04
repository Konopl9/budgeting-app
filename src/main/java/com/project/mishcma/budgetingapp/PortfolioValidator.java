package com.project.mishcma.budgetingapp;

import com.project.mishcma.budgetingapp.entity.Portfolio;
import com.project.mishcma.budgetingapp.service.PortfolioService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;


@RequiredArgsConstructor
@Component
public class PortfolioValidator implements Validator {

	private PortfolioService portfolioService;

	@Override
	public boolean supports(Class<?> clazz) {
		return Portfolio.class.equals(clazz);
	}

	@Override
	public void validate(Object target, Errors errors) {
		Portfolio portfolio = (Portfolio) target;

		// Check for duplicate portfolio name
		if (portfolioService.isPortfolioNameExists(portfolio.getName())) {
			errors.rejectValue("name", "Duplicate.portfolio.name", "Portfolio name already exists");
		}
	}
}

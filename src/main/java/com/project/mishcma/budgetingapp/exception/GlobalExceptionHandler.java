package com.project.mishcma.budgetingapp.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.client.HttpClientErrorException;

@ControllerAdvice
public class GlobalExceptionHandler {

	@ExceptionHandler(StockSymbolNotFoundException.class)
	public ResponseEntity<String> handleStockSymbolNotFoundException(StockSymbolNotFoundException ex) {
		return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND);
	}

	@ExceptionHandler(HttpClientErrorException.class)
	public ResponseEntity<String> handleStockSymbolNotFoundException(HttpClientErrorException ex) {
		return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND);
	}

}

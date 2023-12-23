package com.project.mishcma.budgetingapp.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.mishcma.budgetingapp.DTO.QuoteDTO;
import com.project.mishcma.budgetingapp.DTO.StockDataDTO;
import com.project.mishcma.budgetingapp.DTO.StringList;
import com.project.mishcma.budgetingapp.DTO.SymbolDTO;
import com.project.mishcma.budgetingapp.config.Endpoint;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import com.project.mishcma.budgetingapp.exception.StockDataNotFoundException;
import com.project.mishcma.budgetingapp.exception.StockSymbolNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import static com.project.mishcma.budgetingapp.helper.StringUtils.extractErrorMessage;

@Service
public class MarketDataApiService implements MarketDataService {

  private final Logger logger = LoggerFactory.getLogger(MarketDataApiService.class);

  private final RestTemplate restTemplate;

  public MarketDataApiService(RestTemplate restTemplate) {
    this.restTemplate = restTemplate;
  }

  public Optional<SymbolDTO> getStockSymbolData(String symbol) throws StockSymbolNotFoundException {
    String url =
        UriComponentsBuilder.fromUri(Endpoint.SYMBOL.url()).pathSegment(symbol).build().toString();

    ResponseEntity<String> jsonResponse = restTemplate.getForEntity(url, String.class);

    if (jsonResponse.getStatusCode().value() == HttpStatus.NOT_FOUND.value()) {
      throw new StockSymbolNotFoundException(extractErrorMessage(jsonResponse.getBody()));
    }

    SymbolDTO parsedSymbolDTO = parseSymbolData(jsonResponse.getBody());

    if (parsedSymbolDTO == null) {
      return Optional.empty();
    }

    return Optional.of(parsedSymbolDTO);
  }

  public SymbolDTO parseSymbolData(String jsonResponse) {
    ObjectMapper objectMapper = new ObjectMapper();
    try {
      return objectMapper.readValue(jsonResponse, SymbolDTO.class);
    } catch (JsonProcessingException e) {
      logger.error(
          "Error processing SymbolDTO object from internal API {}: {}",
          jsonResponse,
          e.getMessage());
      return null;
    }
  }

  public Optional<List<SymbolDTO>> postStockSymbolsData(List<String> symbols)
      throws StockSymbolNotFoundException {
    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_JSON);
    HttpEntity<StringList> requestEntity = new HttpEntity<>(new StringList(symbols), headers);

    String url = UriComponentsBuilder.fromUri(Endpoint.SYMBOL.url()).build().toString();

    ResponseEntity<String> jsonResponse =
        restTemplate.postForEntity(url, requestEntity, String.class);

    if (jsonResponse.getStatusCode().value() == HttpStatus.NOT_FOUND.value()) {
      throw new StockSymbolNotFoundException(extractErrorMessage(jsonResponse.getBody()));
    }

    List<SymbolDTO> parsedSymbolDTOS = parseSymbolsData(jsonResponse.getBody());

    if (parsedSymbolDTOS == null
        || parsedSymbolDTOS.isEmpty()
        || parsedSymbolDTOS.size() != symbols.size()) {
      throw new StockSymbolNotFoundException("Unable to find a stock symbol in external API");
    }

    return Optional.of(parsedSymbolDTOS);
  }

  public List<SymbolDTO> parseSymbolsData(String jsonResponse) {
    ObjectMapper objectMapper = new ObjectMapper();
    try {
      return objectMapper.readValue(jsonResponse, new TypeReference<List<SymbolDTO>>() {});
    } catch (JsonProcessingException e) {
      logger.error(
          "Error processing one of the Symbols object from internal API {}: {}",
          jsonResponse,
          e.getMessage());
      return null;
    }
  }

  public Optional<QuoteDTO> getStockQuoteData(String symbol) {
    String url =
        UriComponentsBuilder.fromUri(Endpoint.QUOTE.url()).query(symbol).build().toString();

    ResponseEntity<String> jsonResponse = restTemplate.getForEntity(url, String.class);

    if (jsonResponse.getStatusCode() != HttpStatus.OK) {
      return Optional.empty();
    }

    QuoteDTO parsedQuoteDTO = parseQuoteData(jsonResponse.getBody());

    if (parsedQuoteDTO == null) {
      return Optional.empty();
    }

    return Optional.of(parsedQuoteDTO);
  }

  public QuoteDTO parseQuoteData(String jsonResponse) {
    ObjectMapper objectMapper = new ObjectMapper();
    try {
      return objectMapper.readValue(jsonResponse, QuoteDTO.class);
    } catch (JsonProcessingException e) {
      logger.error(
          "Error processing QuoteDTO object from internal API {}: {}",
          jsonResponse,
          e.getMessage());
      return null;
    }
  }

  public Optional<List<StockDataDTO>> postPortfolioData(Set<String> symbols)
      throws StockDataNotFoundException {
    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_JSON);
    HttpEntity<StringList> requestEntity =
        new HttpEntity<>(new StringList(symbols.stream().toList()), headers);

    String url =
        UriComponentsBuilder.fromUri(Endpoint.PORTFOLIO.url())
            .pathSegment("findStockPrices")
            .build()
            .toString();

    ResponseEntity<String> jsonResponse =
        restTemplate.postForEntity(url, requestEntity, String.class);

    if (jsonResponse.getStatusCode() != HttpStatus.OK) {
      throw new StockDataNotFoundException("Unable to find a stock data in external API");
    }

    List<StockDataDTO> parsedStockDataDTOS = parsePortfolioData(jsonResponse.getBody());

    if (parsedStockDataDTOS == null
        || parsedStockDataDTOS.isEmpty()
        || parsedStockDataDTOS.size() != symbols.size()) {
      throw new StockDataNotFoundException("Unable to find a stock symbol in external API");
    }

    return Optional.of(parsedStockDataDTOS);
  }

  public List<StockDataDTO> parsePortfolioData(String jsonResponse) {
    ObjectMapper objectMapper = new ObjectMapper();
    try {
      return objectMapper.readValue(jsonResponse, new TypeReference<List<StockDataDTO>>() {});
    } catch (JsonProcessingException e) {
      logger.error(
          "Error processing one of the Stock Data object from internal API {}: {}",
          jsonResponse,
          e.getMessage());
      return null;
    }
  }
}

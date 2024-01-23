package com.project.mishcma.budgetingapp.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

@Component
public class ApiUrlBuilder {

  private static final String SYMBOL_PATH = "/symbol";
  private static final String QUOTE_PATH = "/quote";
  private static final String PORTFOLIO_PATH = "/portfolio";
  private static final String PROFILE_PATH = "/profile";

  private final String baseUrl;
  private final UriComponentsBuilder baseUriBuilder;

  public ApiUrlBuilder(@Value("${market.data.api.host}") String baseUrl) {
    this.baseUrl = baseUrl + "/api";
    this.baseUriBuilder = UriComponentsBuilder.newInstance().scheme("http").host(this.baseUrl);
  }

  private String buildBaseUrl() {
    return baseUriBuilder.build().toUriString();
  }

  private String buildPath(String path) {
    return baseUriBuilder.cloneBuilder().path(path).build().toUriString();
  }

  private String buildPathWithSegment(String path, String segment) {
    return baseUriBuilder.cloneBuilder().path(path).pathSegment(segment).build().toUriString();
  }

  public String buildSymbolUrl() {
    return buildPath(SYMBOL_PATH);
  }

  public String buildSymbolUrlWithSegment(String segment) {
    return buildPathWithSegment(SYMBOL_PATH, segment);
  }

  public String buildQuoteUrl() {
    return buildPath(QUOTE_PATH);
  }

  public String buildQuoteUrlWithId(String quoteId) {
    return buildPath(QUOTE_PATH + "/" + quoteId);
  }

  public String buildPortfolioUrl() {
    return buildPath(PORTFOLIO_PATH);
  }

  public String buildPortfolioUrlWithSegment(String segment) {
    return buildPathWithSegment(PORTFOLIO_PATH, segment);
  }

  public String buildProfileUrl(String profileId) {
    return buildPath(PROFILE_PATH + "/" + profileId);
  }
}

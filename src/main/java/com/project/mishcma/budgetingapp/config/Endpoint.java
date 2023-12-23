package com.project.mishcma.budgetingapp.config;

import java.net.URI;

public enum Endpoint {
  SYMBOL("/symbol"),
  QUOTE("/quote"),
  PORTFOLIO("/portfolio"),
  PROFILE("/profile");

  private static final String BASE_URL = "http://localhost:8081/api";

  private final URI url;

  Endpoint(String path) {
    this.url = URI.create(BASE_URL + path);
  }

  public URI url() {
    return url;
  }
}

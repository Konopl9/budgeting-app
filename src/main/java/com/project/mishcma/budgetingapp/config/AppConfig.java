package com.project.mishcma.budgetingapp.config;

import com.project.mishcma.budgetingapp.exception.RestTemplateResponseErrorHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class AppConfig {

  @Bean
  public RestTemplate restTemplate() {
    RestTemplate restTemplate = new RestTemplate();
    restTemplate.setErrorHandler(new RestTemplateResponseErrorHandler());
    return restTemplate;
  }
}

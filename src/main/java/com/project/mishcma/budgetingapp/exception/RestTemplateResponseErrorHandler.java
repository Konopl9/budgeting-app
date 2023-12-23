package com.project.mishcma.budgetingapp.exception;

import static com.project.mishcma.budgetingapp.helper.StringUtils.convertStreamToString;
import static com.project.mishcma.budgetingapp.helper.StringUtils.extractErrorMessage;
import static org.springframework.http.HttpStatus.Series.CLIENT_ERROR;
import static org.springframework.http.HttpStatus.Series.SERVER_ERROR;

import java.io.IOException;
import org.springframework.http.HttpStatus;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.web.client.ResponseErrorHandler;

public class RestTemplateResponseErrorHandler implements ResponseErrorHandler {

  @Override
  public boolean hasError(ClientHttpResponse httpResponse) throws IOException {
    return (httpResponse.getStatusCode().value() == CLIENT_ERROR.value()
        || httpResponse.getStatusCode().value() == SERVER_ERROR.value());
  }

  @Override
  public void handleError(ClientHttpResponse httpResponse) throws IOException {
    if (httpResponse.getStatusCode().is5xxServerError()) {
      // Handle server error
    } else if (httpResponse.getStatusCode().is4xxClientError()) {
      if (httpResponse.getStatusCode() == HttpStatus.NOT_FOUND) {
        String responseBody = convertStreamToString(httpResponse.getBody());
        String errorMessage = extractErrorMessage(responseBody);
        throw new ExternalAPIFetchException(errorMessage);
      }
    }
  }
}

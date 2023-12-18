package com.project.mishcma.budgetingapp.helper;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.stream.Collectors;

public class StringUtils {

	public static String convertStreamToString(InputStream inputStream) throws IOException {
		if (inputStream != null) {
			try (BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream))) {
				return bufferedReader.lines().collect(Collectors.joining(System.lineSeparator()));
			}
		} else {
			return "";
		}
	}

	public static String extractErrorMessage(String responseBody) {
		try {
			JsonNode jsonNode = new ObjectMapper().readTree(responseBody);
			JsonNode messageNode = jsonNode.get("message");
			if (messageNode != null) {
				return messageNode.asText();
			}
		} catch (IOException e) {

		}
		return "Unknown error occurred";
	}
}

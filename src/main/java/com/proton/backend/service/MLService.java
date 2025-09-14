package com.proton.backend.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

@Service
public class MLService {

    @Value("${ml.predict.url}")
    private String predictUrl;

    private final HttpClient httpClient;
    private final ObjectMapper objectMapper;

    public MLService() {
        this.httpClient = HttpClient.newHttpClient();
        this.objectMapper = new ObjectMapper();
    }

    /**
     * Predicts future steps with the given number of hours and optional quantile.
     *
     * @param hours    number of future steps (hours)
     * @param quantile optional quantile, default 0.98
     * @return JSON response from the ML service
     * @throws Exception on HTTP or JSON errors
     */
    public JsonNode predict(int hours, double quantile) throws Exception {
        String jsonBody = String.format("{\"future_steps\": %d, \"quantile\": %f}", hours, quantile);
        return sendPostRequest(jsonBody);
    }

    /**
     * Helper method to send a POST request with JSON body and parse JSON response.
     */
    private JsonNode sendPostRequest(String jsonBody) throws Exception {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(predictUrl))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(jsonBody))
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() != 200) {
            throw new RuntimeException("Failed request: HTTP " + response.statusCode() + " - " + response.body());
        }

        return objectMapper.readTree(response.body());
    }




}

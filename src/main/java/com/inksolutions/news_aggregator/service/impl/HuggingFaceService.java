package com.inksolutions.news_aggregator.service.impl;

import com.inksolutions.news_aggregator.repository.CityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
@RequiredArgsConstructor
public class HuggingFaceService {
    private final RestTemplate restTemplate;
    private final CityRepository cityRepository;

    @Value("${huggingface.api.key}")
    private String apiKey;

    public String determineLocation(String articleTitle) {
        if (articleTitle == null || articleTitle.trim().isEmpty()) {
            throw new IllegalArgumentException("Article title cannot be null or empty");
        }

        String url = "https://api-inference.huggingface.co/models/dslim/bert-base-NER";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(apiKey);

        String requestBody = "{ \"inputs\": \"" + articleTitle + "\" }";

        HttpEntity<String> entity = new HttpEntity<>(requestBody, headers);
        ResponseEntity<String> response = restTemplate.postForEntity(url, entity, String.class);

        String jsonResponse = response.getBody();

        List<String> locations = extractLocationsFromResponse(jsonResponse);

        for (String location : locations) {
            if (isLocationInUSCities(location)) {
                return location;
            }
        }
        return null;
    }

    private List<String> extractLocationsFromResponse(String jsonResponse) {
        Pattern pattern = Pattern.compile("\"word\":\\s*\"(.*?)\"");
        Matcher matcher = pattern.matcher(jsonResponse);
        List<String> locations = new ArrayList<>();

        while (matcher.find()) {
            locations.add(matcher.group(1).trim());
        }
        return locations;
    }

    private boolean isLocationInUSCities(String location) {
        return cityRepository.existsByName(location);
    }
}

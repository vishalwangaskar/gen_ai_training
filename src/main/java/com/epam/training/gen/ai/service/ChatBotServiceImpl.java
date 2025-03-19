package com.epam.training.gen.ai.service;

import com.epam.training.gen.ai.model.ApiRequest;
import com.epam.training.gen.ai.model.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class ChatBotServiceImpl implements ChatBotService {

    @Value("${client-openai-key}")
    String key;

    @Value("${client-openai-endpoint}")
    String apiUrl;

    @Value("${client-openai-deployment-name}")
    String deploymentName;


    private RestTemplate restTemplate;

    private final RestTemplate restTemplate;
    private final Semanticak kernel;

    @Override
    public String connectToDial(ApiRequest apiRequest) {
        return "";
    }



/*
    @Override
    public String connectToDial(ApiRequest apiRequest) {

        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/json");
        headers.set("Api-Key", key);

        ResponseEntity<ApiResponse> response = restTemplate.exchange(
                apiUrl + "/openai/deployments/" + deploymentName + "/chat/completions?",
                HttpMethod.POST,
                new HttpEntity<>(apiRequest, headers),
                ApiResponse.class
        );

        ApiResponse apiResponse = response.getBody();
        if (apiResponse != null && apiResponse.getChoices() != null && !apiResponse.getChoices().isEmpty()) {
            return apiResponse.getChoices().get(0).getMessage().getContent();
        } else {
            return "No content available in the response.";
        }
    }

 */
}

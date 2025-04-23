package com.epam.training.gen.ai.service;

import com.epam.training.gen.ai.model.OpenAiModel;
import com.epam.training.gen.ai.model.OpenAiModelResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DeploymentModelServiceImpl implements DeploymentModelService {

    public static final String OPENAI_DEPLOYMENTS = "/openai/deployments";
    public static final String API_KEY = "Api-Key";
    private final RestTemplate restTemplate;

    @Value("${client-openai-key}")
    private String openAiKey;

    @Value("${client-openai-endpoint}")
    private String openAiEndpoint;

    @Override
    public List<OpenAiModel> getDeploymentModels() {
        var headers = new HttpHeaders();
        headers.add(API_KEY, openAiKey);
        var response = restTemplate.exchange(openAiEndpoint + OPENAI_DEPLOYMENTS, HttpMethod.GET, new HttpEntity<>(headers), OpenAiModelResponse.class);
        return response.getBody().getData();
    }
}

package com.epam.training.gen.ai.model;

import lombok.Data;

import java.util.List;

@Data
public class OpenAiModelResponse {

    private List<OpenAiModel> data;
}

package com.epam.training.gen.ai.model;

import lombok.Data;

@Data
public class ChatBotRequestDto {
    private String prompt;
    private Double temperature = 1D;
    private Integer maxTokens = 2000;
    private String model;
}
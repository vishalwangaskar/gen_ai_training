package com.epam.training.gen.ai.model;

import lombok.Data;

import java.util.List;

@Data
public class ApiResponse {
    private String id;
    private List<Choice> choices;
    private Usage usage;
    private String created;
    private String model;
    private String object;
    private String system_fingerprint;








}

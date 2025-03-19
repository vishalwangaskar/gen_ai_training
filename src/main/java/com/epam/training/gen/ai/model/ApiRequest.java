package com.epam.training.gen.ai.model;

import lombok.Data;

import java.util.List;

@Data
public class ApiRequest {
    private List<Message> messages;

    @Data
    public static class Message {
        private String role;
        private String content;

    }
}
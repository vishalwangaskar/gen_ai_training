package com.epam.training.gen.ai.model;

import lombok.Data;

@Data
    public  class Choice {
        private String finish_reason;
        private int index;
        private Message message;
        
    }
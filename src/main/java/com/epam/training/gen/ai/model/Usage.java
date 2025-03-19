package com.epam.training.gen.ai.model;

import lombok.Data;

@Data
    public  class Usage {
        private int completion_tokens;
        private int prompt_tokens;
        private int total_tokens;
        
    }
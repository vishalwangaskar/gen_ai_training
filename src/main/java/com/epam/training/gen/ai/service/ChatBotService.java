package com.epam.training.gen.ai.service;


public interface ChatBotService {

     String connectToOpenAi(String  request);

     String  executePromptWithSettings(String prompt);

     String  executePromptWithHistory(String prompt);

}

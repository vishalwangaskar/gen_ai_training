package com.epam.training.gen.ai.service;


import com.epam.training.gen.ai.model.ChatBotRequestDto;

public interface ChatBotService {

    String connectToOpenAi(String request);

    String executePromptWithSettings(String prompt);

    String executePromptWithHistory(String prompt);

     String executePromptWithDeploymentModel(ChatBotRequestDto chatBotRequestDto);


}

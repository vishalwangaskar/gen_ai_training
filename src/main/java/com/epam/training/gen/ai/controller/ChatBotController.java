package com.epam.training.gen.ai.controller;

import com.epam.training.gen.ai.model.ChatBotRequestDto;
import com.epam.training.gen.ai.model.OpenAiModel;
import com.epam.training.gen.ai.service.ChatBotService;
import com.epam.training.gen.ai.service.DeploymentModelService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class ChatBotController {

    private final ChatBotService chatBotService;

    private final DeploymentModelService deploymentModelService;


    @PostMapping("/chatbot")
    public ResponseEntity<String> getChatBotResponse(@RequestBody String content) {


        String response = chatBotService.connectToOpenAi(content);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/chatBotWithPrompt")
    public ResponseEntity<String> getChatBotResponseWithPromptWithSettings(@RequestBody String content) {

        String response = chatBotService.executePromptWithSettings(content);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/chatBotWithHistory")
    public ResponseEntity<String> getChatBotResponseWithHistory(@RequestBody String content) {

        String response = chatBotService.executePromptWithHistory(content);
        return ResponseEntity.ok(response);
    }


    @GetMapping("open-ai/models")
    public ResponseEntity<List<OpenAiModel>> getModels() {
        return ResponseEntity.ok(deploymentModelService.getDeploymentModels());
    }

    @PostMapping("/chatBotWithDifferentModel")
    public ResponseEntity<String> chatWithHistory(@RequestBody ChatBotRequestDto chatBotRequestDto) {
        return ResponseEntity.ok(chatBotService.executePromptWithDeploymentModel(chatBotRequestDto));
    }
}

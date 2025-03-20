package com.epam.training.gen.ai.controller;

import com.epam.training.gen.ai.service.ChatBotService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class ChatBotController {

    private final ChatBotService chatBotService;


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


}

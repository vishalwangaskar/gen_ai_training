package com.epam.training.gen.ai.controller;

import com.epam.training.gen.ai.service.ChatBotService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/chatbot")
@RequiredArgsConstructor
public class ChatBotController {

    private  final ChatBotService chatBotService;

    @PostMapping
    public ResponseEntity<String> getChatBotResponse(@RequestBody  String content) {


        String response = chatBotService.connectToOpenAi(content);
        return ResponseEntity.ok(response);
    }

}

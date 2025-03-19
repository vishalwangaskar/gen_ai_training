package com.epam.training.gen.ai.controller;

import com.epam.training.gen.ai.model.ApiRequest;
import com.epam.training.gen.ai.service.ChatBotService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;

@RestController
@RequestMapping("/chatbot")
public class ChatBotController {

    @Autowired
    ChatBotService chatBotService;


    @PostMapping
    public ResponseEntity<String> getChatBotResponse(@RequestBody  String content) {

        ApiRequest apiRequest = new ApiRequest();
        ApiRequest.Message message = new ApiRequest.Message();
        message.setRole("user");
        message.setContent(content);
        apiRequest.setMessages(Collections.singletonList(message));

        String response = chatBotService.connectToDial(apiRequest);
        return ResponseEntity.ok(response);
    }

}

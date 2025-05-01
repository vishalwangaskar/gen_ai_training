package com.epam.training.gen.ai.controller;

import com.epam.training.gen.ai.service.RagService;
import lombok.RequiredArgsConstructor;
import com.microsoft.semantickernel.services.ServiceNotFoundException;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestBody;

import reactor.core.publisher.Mono;


@RestController
@RequiredArgsConstructor
public class RAGBasedController {

    private  final RagService ragService;

    @PostMapping(path = "/rag/chat", consumes = MediaType.APPLICATION_JSON_VALUE)
    public Mono<String> getRagChatResponse(@RequestBody String request) throws ServiceNotFoundException {
        return ragService.getResponse(request);
    }
}

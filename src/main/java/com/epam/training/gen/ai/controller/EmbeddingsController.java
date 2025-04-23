package com.epam.training.gen.ai.controller;

import com.epam.training.gen.ai.model.Score;
import com.epam.training.gen.ai.service.EmbeddingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class EmbeddingsController {

    private final EmbeddingService embeddingService;

    @PostMapping(path = "/embedding/array", consumes = MediaType.APPLICATION_JSON_VALUE)
    public Flux<List<Float>> getEmbeddingArrayResponse(@RequestBody String input) throws Exception {
        return embeddingService.buildEmbedding(input);
    }

    @PostMapping(path = "/embedding/store", consumes = MediaType.APPLICATION_JSON_VALUE)
    public Flux<String> storeResponse(@RequestBody String input) throws Exception {
        return embeddingService.storeEmbedding(input);
    }

    @PostMapping(path = "/embedding/search", consumes = MediaType.APPLICATION_JSON_VALUE)
    public Flux<Score> embeddingSearch(@RequestBody String input) throws Exception {
        return embeddingService.searchEmbedding(input);
    }
}

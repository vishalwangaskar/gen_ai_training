package com.epam.training.gen.ai.service;


import com.epam.training.gen.ai.model.Score;
import reactor.core.publisher.Flux;

import java.util.List;

public interface EmbeddingService {

    Flux<List<Float>> buildEmbedding(String input);

    Flux<String> storeEmbedding(String input);

    Flux<Score> searchEmbedding(String input);
}
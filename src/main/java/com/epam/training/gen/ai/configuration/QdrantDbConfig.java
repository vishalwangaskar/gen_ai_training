package com.epam.training.gen.ai.configuration;

import io.qdrant.client.QdrantClient;
import io.qdrant.client.QdrantGrpcClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class QdrantDbConfig {

    @Bean
    public QdrantClient qdrantClient(EmbeddingsConfig embeddingsconfig)  {
        return new QdrantClient(QdrantGrpcClient.newBuilder("localhost", 6334, false).build());
    }


}
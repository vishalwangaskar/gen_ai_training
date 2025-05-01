package com.epam.training.gen.ai.service;

import com.epam.training.gen.ai.configuration.EmbeddingsConfig;
import io.qdrant.client.QdrantClient;
import io.qdrant.client.grpc.Collections.Distance;
import io.qdrant.client.grpc.Collections.VectorParams;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.stereotype.Component;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

@Slf4j
@Component
@RequiredArgsConstructor
public class QdrantInitializer {

    private final QdrantClient client;
    private final EmbeddingService embeddingService;
    private final EmbeddingsConfig embeddingsConfig;

    @EventListener
    public void initQdrant(ApplicationReadyEvent event) throws InterruptedException, ExecutionException {
        log.info("Qdrant initialization");
        ensureCollectionExists();
        log.info("Qdrant initialized");
    }

    private void ensureCollectionExists() throws InterruptedException, ExecutionException {
        var exists = client.collectionExistsAsync(embeddingsConfig.getCollectionName()).get();
        if (!exists) {
            client.createCollectionAsync(embeddingsConfig.getCollectionName(), VectorParams.newBuilder().setDistance(Distance.Cosine).setSize(embeddingsConfig.getVectorSize()).build()).get();
            var contents = readFiles(embeddingsConfig.getInitDocsPath());
            contents.forEach(content -> embeddingService.buildEmbeddingAndStore(content).subscribe());
        }
    }

    public static List<String> readFiles(String path) {
        List<String> contents = new ArrayList<>();
        try {
            PathMatchingResourcePatternResolver scanner = new PathMatchingResourcePatternResolver();
            Resource[] resources = scanner.getResources(path);
            if (resources == null || resources.length == 0) {
                log.warn("Resources not found {} ", path);
                return contents;
            }
            for (Resource resource : resources) {
                log.info(resource.getFilename());
                contents.add(resource.getContentAsString(Charset.defaultCharset()));
            }
        } catch (Exception e) {
            throw new RuntimeException("Failed to read the resources folder:" + e.getMessage(), e);
        }
        return contents;
    }
}
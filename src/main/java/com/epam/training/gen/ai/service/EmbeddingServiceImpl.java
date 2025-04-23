package com.epam.training.gen.ai.service;

import com.azure.ai.openai.OpenAIAsyncClient;
import com.azure.ai.openai.models.EmbeddingItem;
import com.azure.ai.openai.models.Embeddings;
import com.azure.ai.openai.models.EmbeddingsOptions;
import com.epam.training.gen.ai.configuration.EmbeddingsConfig;
import com.epam.training.gen.ai.model.Score;
import com.google.common.util.concurrent.FutureCallback;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;
import io.qdrant.client.QdrantClient;
import io.qdrant.client.WithPayloadSelectorFactory;
import io.qdrant.client.grpc.Points.PointStruct;
import io.qdrant.client.grpc.Points.SearchPoints;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.task.TaskExecutor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.UUID;

import static io.qdrant.client.PointIdFactory.id;
import static io.qdrant.client.ValueFactory.value;
import static io.qdrant.client.VectorsFactory.vectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class EmbeddingServiceImpl implements EmbeddingService {

    private final QdrantClient qdrantClient;
    private final EmbeddingsConfig embeddingsconfig;
    private final OpenAIAsyncClient openAiClient;
    private final TaskExecutor taskExecutor;

    public Flux<List<Float>> buildEmbedding(String input) {
        var options = new EmbeddingsOptions(List.of(input.replace("\n", "")));
        options.setDimensions(embeddingsconfig.getVectorSize());
        return openAiClient.getEmbeddings(embeddingsconfig.getDeploymentName(), options).flatMapIterable(Embeddings::getData)
                .map(EmbeddingItem::getEmbedding).doOnError(t -> log.error("Exception while embedding creation", t));
    }

    public Flux<String> storeEmbedding(String input) {
        return buildEmbedding(input)
                .map(vector -> PointStruct.newBuilder().setId(id(UUID.nameUUIDFromBytes(input.getBytes()))).setVectors(vectors(vector)).putPayload("text", value(input))
                        .build()).buffer().map(points -> qdrantClient.upsertAsync(embeddingsconfig.getCollectionName(), points))
                .flatMap(this::toMono).map(result -> result.getStatus().name());
    }

    public Flux<Score> searchEmbedding(String input) {
        return buildEmbedding(input).map(this::createSearchPoints).map(qdrantClient::searchAsync).flatMap(this::toMono).flatMapIterable(points -> points)
                .map(point -> new Score(point.getId().getUuid(), point.getScore(), point.getPayloadOrDefault("text", value("(no data)")).getStringValue()));
    }

    private SearchPoints createSearchPoints(List<Float> vector) {
        return SearchPoints.newBuilder().setCollectionName(embeddingsconfig.getCollectionName()).addAllVector(vector).setLimit(embeddingsconfig.getSearchLimit())
                .setWithPayload(WithPayloadSelectorFactory.enable(true)).build();
    }

    private <T> Mono<T> toMono(ListenableFuture<T> future) {
        return Mono.create(sink -> {
            Futures.addCallback(future, new FutureCallback<T>() {
                @Override
                public void onSuccess(T result) {
                    sink.success(result);
                }

                @Override
                public void onFailure(Throwable throwable) {
                    sink.error(throwable);
                }
            }, taskExecutor);
        });
    }
}
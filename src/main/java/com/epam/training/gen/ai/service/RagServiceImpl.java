package com.epam.training.gen.ai.service;

import com.microsoft.semantickernel.Kernel;
import com.microsoft.semantickernel.orchestration.InvocationContext;
import com.microsoft.semantickernel.services.ServiceNotFoundException;
import com.microsoft.semantickernel.services.chatcompletion.ChatCompletionService;
import com.microsoft.semantickernel.services.chatcompletion.ChatHistory;
import com.microsoft.semantickernel.services.chatcompletion.ChatMessageContent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class RagServiceImpl implements  RagService{

    private final Kernel semanticKernel;
    private final InvocationContext invocationContext;
    private final EmbeddingService embeddingService;

    private boolean isNewHistory = true;
    private static final String RESPONSE_MESSAGE = "Provide answers from CONTEXT only.If enough information is not present in the CONTEXT then return 'I do not know' ";
    private AtomicReference<ChatHistory> chatHistoryAtomicReference = new AtomicReference<>(createHistory());

    @Override
    public Mono<String> getResponse(String prompt) throws ServiceNotFoundException {
        var context = getContext(prompt);
        var chatHistory = getChatHistory();
        chatHistory.addUserMessage("""
                    <CONTEXT>
                        ${context}
                    </CONTEXT>
                    <USER_QUERY>
                        ${prompt}
                    </USER_QUERY>
                """
                .replace("${prompt}", prompt).replace("${context}", context)
        );
        var chatCompletionService = semanticKernel.getService(ChatCompletionService.class);
        return chatCompletionService.getChatMessageContentsAsync(chatHistory, semanticKernel, invocationContext).doOnNext(this::updateHistory).map(RagServiceImpl::convertMessagesToString).doOnNext(response -> log.info("""
                Input prompt: ```
                ${prompt}
                ```
                Model response: ```
                ${response}
                ```
                """
                .replace("${prompt}", prompt).replace("${response}", response)));
    }

    private void updateHistory(List<ChatMessageContent<?>> chatMessageList) {
        var chatHistory = getChatHistory();
        chatHistory.addAll(chatMessageList);
    }

    private static String convertMessagesToString(List<ChatMessageContent<?>> chatMessageList) {
        return chatMessageList.stream().filter(content -> content.getContent() != null).map(content -> content.getContent()).collect(Collectors.joining("\n"));
    }

    private ChatHistory getChatHistory() {
        if (isNewHistory) {
            isNewHistory = false;
            return chatHistoryAtomicReference.updateAndGet(prev -> createHistory());
        }
        return chatHistoryAtomicReference.get();
    }

    private ChatHistory createHistory() {
        return new ChatHistory(RESPONSE_MESSAGE);
    }

    private String getContext(String prompt) {
        return embeddingService.searchEmbedding(prompt).map(result -> result.originalText()).reduce((a, b) -> a + "\n" + b).block();
    }
}
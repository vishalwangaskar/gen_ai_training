package com.epam.training.gen.ai.service;

import com.azure.ai.openai.OpenAIAsyncClient;
import com.epam.training.gen.ai.model.ChatBotRequestDto;
import com.microsoft.semantickernel.Kernel;
import com.microsoft.semantickernel.orchestration.InvocationContext;
import com.microsoft.semantickernel.orchestration.PromptExecutionSettings;
import com.microsoft.semantickernel.semanticfunctions.KernelFunctionArguments;
import com.microsoft.semantickernel.services.chatcompletion.AuthorRole;
import com.microsoft.semantickernel.services.chatcompletion.ChatCompletionService;
import com.microsoft.semantickernel.services.chatcompletion.ChatHistory;
import com.microsoft.semantickernel.services.chatcompletion.ChatMessageContent;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Lookup;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class ChatBotServiceImpl implements ChatBotService {

    private final Kernel kernel;

    private final ChatCompletionService chatCompletionService;

    private final InvocationContext invocationContext;

    private final OpenAIAsyncClient openAIAsyncClient;

    ChatHistory chatHistory = new ChatHistory();



    @Override
    public String connectToOpenAi(String request) {
        return Objects.requireNonNull(Objects.requireNonNull(kernel.invokePromptAsync(request).block()).getResult()).toString();

    }

    @Override
    public String executePromptWithSettings(String prompt) {


        return Objects.requireNonNull(Objects.requireNonNull(kernel.invokePromptAsync(prompt, KernelFunctionArguments.builder().withInput(prompt).build(), invocationContext).block()).getResult()).toString();
    }

    @Override
    public String executePromptWithHistory(String prompt) {
        do {
            chatHistory.addUserMessage(prompt);

            List<ChatMessageContent<?>> results = chatCompletionService
                    .getChatMessageContentsAsync(chatHistory, kernel, invocationContext)
                    .block();

            for (ChatMessageContent<?> result : results) {
                if (result.getAuthorRole() == AuthorRole.ASSISTANT && result.getContent() != null) {
                    return result.getContent();
                }
                chatHistory.addMessage(result);
            }
        } while (prompt != null && !prompt.isEmpty());

        return "invalid content";
    }


    @Override
    public String executePromptWithDeploymentModel(ChatBotRequestDto chatBotRequestDto) {

       var completionService = getChatCompletionService(chatBotRequestDto.getModel(), openAIAsyncClient);
        chatHistory.addUserMessage(chatBotRequestDto.getPrompt());
        var response = completionService.getChatMessageContentsAsync(
                chatHistory, getKernel(chatCompletionService),
                new InvocationContext.Builder().withPromptExecutionSettings(PromptExecutionSettings.builder().withTemperature(chatBotRequestDto.getTemperature())
                        .withMaxTokens(chatBotRequestDto.getMaxTokens()).build()).build()).block();
        var responseResult = new StringBuilder();
        if (response == null || response.isEmpty()) {
            return StringUtils.EMPTY;
        }
        response.stream().filter(result -> result.getAuthorRole() == AuthorRole.ASSISTANT).forEach(result -> {
            chatHistory.addAssistantMessage(result.getContent());
            responseResult.append(result.getContent());
        });
        return responseResult.toString();
    }

    @Lookup("chatCompletionService")
    protected ChatCompletionService getChatCompletionService(String model, OpenAIAsyncClient openAIAsyncClient) {
        return null;
    }

    @Lookup("kernel")
    protected Kernel getKernel(ChatCompletionService chatCompletionService) {
        return null;
    }
}


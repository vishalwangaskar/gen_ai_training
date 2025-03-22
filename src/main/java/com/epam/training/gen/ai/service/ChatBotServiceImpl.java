package com.epam.training.gen.ai.service;

import com.microsoft.semantickernel.Kernel;
import com.microsoft.semantickernel.orchestration.InvocationContext;
import com.microsoft.semantickernel.semanticfunctions.KernelFunctionArguments;
import com.microsoft.semantickernel.services.chatcompletion.AuthorRole;
import com.microsoft.semantickernel.services.chatcompletion.ChatCompletionService;
import com.microsoft.semantickernel.services.chatcompletion.ChatHistory;
import com.microsoft.semantickernel.services.chatcompletion.ChatMessageContent;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class ChatBotServiceImpl implements ChatBotService {

    private final Kernel kernel;

    private final ChatCompletionService chatCompletionService;

    private final InvocationContext invocationContext;


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
            ChatHistory history = new ChatHistory();
            history.addUserMessage(prompt);

            List<ChatMessageContent<?>> results = chatCompletionService
                    .getChatMessageContentsAsync(history, kernel, invocationContext)
                    .block();

            for (ChatMessageContent<?> result : results) {
                if (result.getAuthorRole() == AuthorRole.ASSISTANT && result.getContent() != null) {
                    return result.getContent();
                }
                history.addMessage(result);
            }
        } while (prompt != null && !prompt.isEmpty());

        return "invalid content";
    }

}


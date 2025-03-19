package com.epam.training.gen.ai.service;

import com.microsoft.semantickernel.Kernel;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
@RequiredArgsConstructor
public class ChatBotServiceImpl implements ChatBotService {

    private final Kernel kernel;

    @Override
    public String connectToOpenAi(String request) {
        String response = Objects.requireNonNull(Objects.requireNonNull(kernel.invokePromptAsync(request).block()).getResult()).toString();

        return Objects.requireNonNullElse(response, "No content available in the response.");
    }

}

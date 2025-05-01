package com.epam.training.gen.ai.configuration;

import com.microsoft.semantickernel.orchestration.PromptExecutionSettings;
import com.microsoft.semantickernel.semanticfunctions.KernelFunction;
import com.microsoft.semantickernel.semanticfunctions.KernelFunctionFromPrompt;
import com.microsoft.semantickernel.semanticfunctions.OutputVariable;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class KernelFunctionConfig {
    public KernelFunction<String> buildKernelFunction(String prompt) throws IOException {
        return KernelFunctionFromPrompt.builder()
                .withTemplate(prompt)
                .withDefaultExecutionSettings(
                        PromptExecutionSettings.builder()
                                .withTemperature(0.2)
                                .withMaxTokens(100)
                                .withModelId("text-embedding-ada-002")
                                .build())
                .withOutputVariable(new OutputVariable<>("response", String.class))
                .build();
    }
}

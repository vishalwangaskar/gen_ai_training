package com.epam.training.gen.ai.configuration;

import com.azure.ai.openai.OpenAIAsyncClient;
import com.azure.ai.openai.OpenAIClientBuilder;
import com.azure.core.credential.AzureKeyCredential;
import com.epam.training.gen.ai.model.Plugin;
import com.epam.training.gen.ai.plugin.AgeBasedOnBirthday;
import com.epam.training.gen.ai.plugin.ConvertTemperaturePlugin;
import com.microsoft.semantickernel.Kernel;
import com.microsoft.semantickernel.aiservices.openai.chatcompletion.OpenAIChatCompletion;
import com.microsoft.semantickernel.orchestration.InvocationContext;
import com.microsoft.semantickernel.orchestration.PromptExecutionSettings;
import com.microsoft.semantickernel.plugin.KernelPlugin;
import com.microsoft.semantickernel.plugin.KernelPluginFactory;
import com.microsoft.semantickernel.services.chatcompletion.ChatCompletionService;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Configuration
public class KernelConfiguration {

    @Value("${client-openai-key}")
    private String openAiKey;

    @Value("${client-openai-endpoint}")
    private String openAiEndpoint;

    @Value("${client-openai-deployment-name}")
    private String openAiDefaultDeploymentName;


    @Bean
    public OpenAIAsyncClient openAIAsyncClient() {
        return new OpenAIClientBuilder()
                .credential(new AzureKeyCredential(openAiKey))
                .endpoint(openAiEndpoint)
                .buildAsyncClient();
    }

    @Bean
    public ChatCompletionService chatCompletionService(OpenAIAsyncClient openAIAsyncClient) {
        return OpenAIChatCompletion.builder()
                .withModelId(openAiDefaultDeploymentName)
                .withOpenAIAsyncClient(openAIAsyncClient)
                .build();
    }

    @Bean
    public KernelPlugin kernelPlugin() {
        return KernelPluginFactory.createFromObject(
                new Plugin(), "Plugin");
    }


    @Bean
    public Kernel kernel(ChatCompletionService chatCompletionService, KernelPlugin kernelPlugin) {
        var convertTemperaturePlugin =
                KernelPluginFactory.createFromObject(new ConvertTemperaturePlugin(), "ConvertTemperaturePlugin");
        var ageBasedOnBirthdayPlugin =
                KernelPluginFactory.createFromObject(new AgeBasedOnBirthday(), "AgeBasedOnBirthday");
        return Kernel.builder()
                .withAIService(ChatCompletionService.class, chatCompletionService)
                .withPlugin(convertTemperaturePlugin)
                .withPlugin(ageBasedOnBirthdayPlugin)
                .build();
    }


    @Bean
    public InvocationContext invocationContext() {
        return InvocationContext.builder()
                .withPromptExecutionSettings(PromptExecutionSettings.builder()
                        .withTemperature(0)
                        .build())
                .build();
    }

    @Bean
    public Map<String, PromptExecutionSettings> promptExecutionsSettingsMap(
            @Value("${client-openai-deployment-name}") String deploymentOrModelName) {
        return Map.of(deploymentOrModelName, PromptExecutionSettings.builder()
                .withTemperature(0.8)

                .build());
    }

    @Bean
    @Scope(value = "prototype")
    public Kernel kernelWithChatCompletionService(final ChatCompletionService chatCompletionService) {
        return Kernel.builder()
                .withAIService(ChatCompletionService.class, chatCompletionService)
                .build();
    }

    @Bean
    @Scope(value = "prototype")
    public ChatCompletionService chatCompletionServiceWithDeploymentModel(
            @Value("${client-openai-deployment-name}") String deploymentModelName,
            final OpenAIAsyncClient openAIAsyncClient) {
        return OpenAIChatCompletion.builder()
                .withModelId(deploymentModelName)
                .withModelId(ObjectUtils.isEmpty(deploymentModelName)
                        ? openAiDefaultDeploymentName : deploymentModelName)
                .withOpenAIAsyncClient(openAIAsyncClient)
                .build();
    }


    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

}


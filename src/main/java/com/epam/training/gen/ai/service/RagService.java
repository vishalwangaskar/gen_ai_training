package com.epam.training.gen.ai.service;

import com.microsoft.semantickernel.services.ServiceNotFoundException;
import reactor.core.publisher.Mono;

public interface RagService {

     Mono<String> getResponse(String prompt) throws ServiceNotFoundException;
}

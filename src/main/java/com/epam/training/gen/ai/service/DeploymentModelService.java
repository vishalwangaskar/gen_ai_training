package com.epam.training.gen.ai.service;

import com.epam.training.gen.ai.model.OpenAiModel;

import java.util.List;

public interface DeploymentModelService {

    List<OpenAiModel> getDeploymentModels();

}

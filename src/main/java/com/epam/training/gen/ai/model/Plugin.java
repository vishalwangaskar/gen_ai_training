package com.epam.training.gen.ai.model;

import com.microsoft.semantickernel.semanticfunctions.annotations.DefineKernelFunction;
import com.microsoft.semantickernel.semanticfunctions.annotations.KernelFunctionParameter;

public class Plugin {

    @DefineKernelFunction(name = "searchForResults", description = "Searches for information in web.")
    public String searchForQueryResults(@KernelFunctionParameter(description = "Searches for information based on query in web.", name = "query") String query) {
        return query;
    }
}
package com.epam.training.gen.ai.plugin;

import com.microsoft.semantickernel.semanticfunctions.annotations.DefineKernelFunction;
import com.microsoft.semantickernel.semanticfunctions.annotations.KernelFunctionParameter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

@Slf4j
public class ConvertTemperaturePlugin {

    public static final String CELSIUS = "Celsius";
    public static final String FAHRENHEIT = "Fahrenheit";
    public static final String KELVIN = "Kelvin";

    @DefineKernelFunction(name = "convert_temperature", description = "Converts temperature from one scale to another (Celsius, Fahrenheit, Kelvin).")
    public String convertTemperature(
            @KernelFunctionParameter(name = "temperature", description = "Temperature value", type = String.class) String temperature,
            @KernelFunctionParameter(name = "fromScale", description = "Scale to convert from (Celsius, Fahrenheit, Kelvin)", type = String.class) String fromScale,
            @KernelFunctionParameter(name = "toScale", description = "Scale to convert to (Celsius, Fahrenheit, Kelvin)", type = String.class) String toScale) {

        var convertedAmount = String.valueOf(temperature);
        if (StringUtils.compareIgnoreCase(fromScale, CELSIUS) == 0) {
            if (StringUtils.compareIgnoreCase(toScale, FAHRENHEIT) == 0) {
                convertedAmount = String.valueOf(Double.parseDouble(temperature) * 9 / 5 + 32);
            } else if (StringUtils.compareIgnoreCase(toScale, KELVIN) == 0) {
                convertedAmount = String.valueOf(Double.parseDouble(temperature) + 273.15);
            }
        } else if (StringUtils.compareIgnoreCase(fromScale, FAHRENHEIT) == 0) {
            if (StringUtils.compareIgnoreCase(toScale, CELSIUS) == 0) {
                convertedAmount = String.valueOf((Double.parseDouble(temperature) - 32) * 5 / 9);
            } else if (StringUtils.compareIgnoreCase(toScale, KELVIN) == 0) {
                convertedAmount = String.valueOf((Double.parseDouble(temperature) - 32) * 5 / 9 + 273.15);
            }
        } else if (StringUtils.compareIgnoreCase(fromScale, KELVIN) == 0) {
            if (StringUtils.compareIgnoreCase(toScale, CELSIUS) == 0) {
                convertedAmount = String.valueOf(Double.parseDouble(temperature) - 273.15);
            } else if (StringUtils.compareIgnoreCase(toScale, FAHRENHEIT) == 0) {
                convertedAmount = String.valueOf((Double.parseDouble(temperature) - 273.15) * 9 / 5 + 32);
            }
        } else if ((StringUtils.compareIgnoreCase(fromScale, toScale) == 0)) {
            convertedAmount = String.valueOf(temperature);
        } else {
            throw new IllegalArgumentException("Unsupported temperature conversion: " + fromScale + " to " + toScale);
        }
        log.info("Plugin Logic converting {} {} to {} {}", temperature, fromScale, toScale, convertedAmount);
        return convertedAmount;
    }
}
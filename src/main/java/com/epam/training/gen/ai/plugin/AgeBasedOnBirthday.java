package com.epam.training.gen.ai.plugin;

import com.microsoft.semantickernel.semanticfunctions.annotations.DefineKernelFunction;
import com.microsoft.semantickernel.semanticfunctions.annotations.KernelFunctionParameter;
import lombok.extern.slf4j.Slf4j;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

@Slf4j
public class AgeBasedOnBirthday {

    public static final String[] PATTERNS = new String[]{"yyyy-MM-dd", "dd-MM-yyyy", "MMM dd, yyyy", "dd MMM yyyy"};

    @DefineKernelFunction(name = "calculate_age_by_birthday", description = "Calculates age based on birthday.")
    public String calculateAgeBasedOnBirthday(
            @KernelFunctionParameter(name = "birthDay", description = "The day of birth", type = String.class) String birthDay) {
        var currentDate = getCurrentDate();
        var birthDayDate = convertStringToDate(birthDay);
        log.info("Plugin Logic Calculating the age based on birth year.");
        return String.valueOf(currentDate.getYear() - birthDayDate.getYear());
    }

    private Date getCurrentDate() {
        return new Date();
    }

    private Date convertStringToDate(String dateString) {
        if (dateString == null || dateString.trim().isEmpty()) {
            return null;
        }
        // Try each pattern
        for (var pattern : PATTERNS) {
            try {
                var simpleDateFormat = new SimpleDateFormat(pattern);
                simpleDateFormat.setLenient(false);
                return simpleDateFormat.parse(dateString);
            } catch (ParseException e) {
                // Continue to next pattern
            }
        }
        throw new IllegalArgumentException("Unable to parse date string: " + dateString);
    }
}
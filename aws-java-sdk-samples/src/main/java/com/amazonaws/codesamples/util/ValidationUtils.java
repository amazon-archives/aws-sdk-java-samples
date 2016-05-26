package com.amazonaws.codesamples.util;

import com.amazonaws.codesamples.exception.SampleRetrievalException;

/**
 * Sample retrieval validation helper methods.
 */
public class ValidationUtils {

    public static String assertInputNotEmpty(final String input,
            final String message) {
        if (input == null || input.isEmpty()) {
            throw new IllegalArgumentException(message);
        }
        return input;
    }

    public static String assertResultNotEmpty(final String result,
            final String serviceName, final String sampleId,
            final String message) {
        if (result == null || result.isEmpty()) {
            throw new SampleRetrievalException(message, serviceName, sampleId);
        }
        return result;
    }
    
    public static boolean areNullOrEmpty(final String... strings) {
        for (String str : strings) {
            if (isNullOrEmpty(str)) {
                return true;
            }
        }
        return false;
    }
    
    public static boolean isNullOrEmpty(final String string) {
        return string == null || string.length() == 0;
    }

}

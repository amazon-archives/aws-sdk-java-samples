/*
 * Copyright 2013-2015 Amazon.com, Inc. or its affiliates. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License").
 * You may not use this file except in compliance with the License.
 * A copy of the License is located at
 *
 *  http://aws.amazon.com/apache2.0
 *
 * or in the "license" file accompanying this file. This file is distributed
 * on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either
 * express or implied. See the License for the specific language governing
 * permissions and limitations under the License.
 */
package com.amazonaws.util;

import java.io.File;
import java.util.List;

import com.amazonaws.codesamples.exception.SampleParsingException;

/**
 * Utility methods to assist in sample parsing.
 */
public final class ParsingUtils {

    /**
     * Determine if a given file matches the target extension.
     *
     * @param file
     *            file to check
     * @param targetExtension
     *            target extension
     * @return true if file has target extension
     */
    public static boolean fileMatchesExtension(final File file,
            final String targetExtension) {
        String fileName = file.getName();
        int i = fileName.lastIndexOf(".");
        if (i > 0) {
            String actualExtension = fileName.substring(i + 1);
            if (targetExtension.equals(actualExtension)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Formats a section of sample text by removing leading whitespace
     * characters up to a given maximum and by flattening lines into a single
     * string.
     *
     * @param lines
     *            list where each element represents an individual line of
     *            sample text
     * @param maxTrim
     *            maximum number of leading whitespace characters to trim from
     *            each line
     * @return formatted sample text string
     */
    public static String formatSampleText(final List<String> lines,
            final int maxTrim) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < lines.size(); i++) {
            sb.append(trimLeadingWhitespace(lines.get(i), maxTrim));
            if (i + 1 < lines.size()) {
                sb.append("\n");
            }
        }
        return sb.toString();
    }

    /**
     * Gets the minimum amount of leading whitespace in a text block.
     *
     * @param lines
     *            list where each element represents an individual line of
     *            sample text
     *
     * @return minimum amount of leading whitespace
     */
    public static int getMinWhitespace(final List<String> lines) {
        int minWhitespace = 0;
        for (String line : lines) {
            int lineLeadingWhitespace = getLeadingWhitespace(line);
            if (lineLeadingWhitespace < minWhitespace || minWhitespace == 0) {
                minWhitespace = lineLeadingWhitespace;
            }
        }
        return minWhitespace;
    }

    /**
     * Returns the number of leading whitespace characters in a given string.
     *
     * @param str
     *            string to check
     * @return number of leading whitespace characters
     */
    public static int getLeadingWhitespace(final String str) {
        int pos = 0;
        while ((pos < str.length()) && (str.charAt(pos) == ' ')) {
            pos++;
        }
        return pos;
    }

    /**
     * Trims up to a certain number of leading whitespace characters from a
     * string.
     *
     * @param str
     *            string to process
     * @param max
     *            maximum number of whitespace characters to trim
     * @return trimmed String
     */
    public static String trimLeadingWhitespace(final String str, final int max) {
        int pos = Math.min(getLeadingWhitespace(str), max);
        if (pos > 0) {
            return str.substring(pos);
        } else {
            return str;
        }
    }

    /**
     * Asserts that a specified field contains a non-null, non-empty value.
     *
     * @param field
     *            name of field
     * @param value
     *            value from field
     * @return original value of field, if valid
     */
    public static String assertFieldHasContent(final String field,
            final String value) {
        if (value == null || value.isEmpty()) {
            throw new SampleParsingException(
                    String.format("Required field %s was not provided!",
                            field));
        }
        return value;
    }

}

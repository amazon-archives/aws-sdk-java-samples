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
package com.amazonaws.codesamples.exception;

import java.io.File;

/**
 * Exception thrown when an error occurs while parsing a source file for code
 * samples.
 */
public class SampleParsingException extends RuntimeException {

    private static final long serialVersionUID = 1474663474079249583L;

    /**
     * Constructs a SampleParsingException with a specified message and
     * referencing a file and line number being processed when the error
     * occurred.
     *
     * @param message
     *            exception detail message
     * @param file
     *            file being parsed when exception occurred
     * @param lineNum
     *            line being parsed when exeception occurred
     */
    public SampleParsingException(final String message, final File file,
            final int lineNum) {
        this(message
                + String.format(" (File: %s, Line: %s)", file.getName(),
                        lineNum));
    }

    /**
     * Constructs a SampleParsingException with a specified message.
     *
     * @param message
     *            exception detail message
     */
    public SampleParsingException(final String message) {
        super(message);
    }

}

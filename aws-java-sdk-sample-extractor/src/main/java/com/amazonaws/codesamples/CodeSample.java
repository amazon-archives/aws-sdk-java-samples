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
package com.amazonaws.codesamples;

import java.util.ArrayList;
import java.util.List;

import com.amazonaws.util.ParsingUtils;

/**
 * Represents an individual code sample.
 */
public class CodeSample {

    private final String id;
    private final String serviceName;

    private String title;
    private String description;
    private List<String> codeLines;

    /**
     * Ctor.
     *
     * @param sampleId
     *            unique identifier for code sample
     * @param serviceName
     *            service sample is associated with
     */
    public CodeSample(final String sampleId, final String serviceName) {
        if (sampleId == null || sampleId.isEmpty()) {
            throw new IllegalArgumentException("Sample ID must be provided!");
        }

        if (serviceName == null || serviceName.isEmpty()) {
            throw new IllegalArgumentException("Service name must be provided!");
        }

        this.id = sampleId;
        this.serviceName = serviceName;
        this.codeLines = new ArrayList<String>();
    }

    /**
     * Returns the ID of this sample.
     *
     * @return sample ID
     */
    public String getId() {
        return id;
    }

    /**
     * Returns the name of the service this sample was written for.
     *
     * @return service name
     */
    public String getServiceName() {
        return serviceName;
    }

    /**
     * Returns the title of this sample.
     *
     * @return sample title
     */
    public String getTitle() {
        return title;
    }

    /**
     * Returns the description for this sample, or null if none was provided.
     *
     * @return description if provided
     */
    public String getDescription() {
        return description;
    }

    /**
     * Returns source code for this Sample. Code is formatted and shifted to
     * avoid extraneous leading whitespace.
     *
     * @return formatted sample code
     */
    public String getFormattedCode() {
        int offset = ParsingUtils.getMinWhitespace(codeLines);

        return ParsingUtils.formatSampleText(codeLines, offset);
    }

    /**
     * Set the title for this sample.
     *
     * @param title
     *            sample title
     */
    public void setTitle(final String title) {
        this.title = title;
    }

    /**
     * Set the description for this sample.
     *
     * @param description
     *            sample description
     */
    public void setDescription(final String description) {
        this.description = description;
    }

    /**
     * Append a line of code to this sample.
     *
     * @param line
     *            line of code
     */
    public void appendLine(final String line) {
        codeLines.add(line);
    }

}

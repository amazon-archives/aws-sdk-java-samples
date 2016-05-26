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
package com.amazonaws.codesamples.util;

import java.util.List;

import com.amazonaws.codesamples.RetrievedCodeSample;

/**
 * Templates used to insert raw code samples into Javadoc HTML blocks.
 */
public class SampleHTMLTemplates {

    private static final String SAMPLE_TAG = "${SAMPLES}";
    private static final String TITLE_TAG = "${TITLE}";
    private static final String DESCRIPTION_TAG = "${DESCRIPTION}";
    private static final String CONTENT_TAG = "${CONTENT}";

    private static final String SAMPLES_SECTION = "<br/>"
            + "<b>Samples:</b>"
            + "<ul class=\"sampleList\"><li class=\"sampleList\">"
            + SAMPLE_TAG
            + "</li></ul>";

    private static final String SAMPLE = "<h5>" + TITLE_TAG + "</h5>"
            + "<div class=\"block\">" + DESCRIPTION_TAG + "<br/>"
            + " <pre class=\"brush: java\">" + CONTENT_TAG + "</pre></div>";
    /**
     * Parses a given raw code sample into a HTML sample block.
     *
     * @param title
     *            code sample title
     * @param description
     *            code sample description
     * @param content
     *            code sample content
     * @return formatted HTML code sample block
     */
    public static String getSampleHTML(final String title,
            final String description, final String content) {
        return SAMPLE.replace(TITLE_TAG, title)
                .replace(DESCRIPTION_TAG, description)
                .replace(CONTENT_TAG, content);
    }

    /**
     * Injects HTML formatted samples into an outer samples HTML block.
     *
     * @param samples
     *            HTML formatted samples
     * @return formatted outer HTML samples block
     */
    public static String getSampleSectionHTML(
            final List<RetrievedCodeSample> samples) {
        StringBuilder samplesHTML = new StringBuilder();

        for (RetrievedCodeSample sample : samples) {
            samplesHTML.append(getSampleHTML(sample.getTitle(),
                    sample.getDescription(),
                    sample.getContent()));
        }

        return SAMPLES_SECTION.replace(SAMPLE_TAG, samplesHTML.toString());
    }

}

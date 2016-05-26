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

import org.xml.sax.Attributes;
import org.xml.sax.helpers.DefaultHandler;

/**
 * SAX handler for parsing XML formatted Javadoc sample files.
 */
public class SampleCodeXMLParser extends DefaultHandler {

    private static final String SAMPLE_ELEMENT = "sample";
    private static final String CONTENT_ELEMENT = "content";
    private static final String ID_ATTRIBUTE = "id";
    private static final String TITLE_ATTRIBUTE = "title";
    private static final String DESCRIPTION_ATTRIBUTE = "description";

    private boolean inTargetSample;
    private boolean inTargetContent;
    private StringBuffer sampleBuffer;

    private final String sampleId;

    private String sampleTitle;
    private String sampleDescription;
    private String sampleContent;

    public SampleCodeXMLParser(final String sampleId) {
        this.inTargetSample = false;
        this.inTargetContent = false;
        this.sampleBuffer = new StringBuffer();
        this.sampleId = sampleId;
    }

    @Override
    public void startElement(String uri, String localName, String qName,
            Attributes attributes) {
        if (qName.equals(SAMPLE_ELEMENT)) {
            String idAttr = attributes.getValue(ID_ATTRIBUTE);
            if (idAttr != null && idAttr.equals(sampleId)) {
                inTargetSample = true;
                sampleTitle = attributes.getValue(TITLE_ATTRIBUTE);
                sampleDescription = attributes.getValue(DESCRIPTION_ATTRIBUTE);
            }
        }

        if (qName.equals(CONTENT_ELEMENT) && inTargetSample) {
            inTargetContent = true;
        }

    }

    @Override
    public void characters(char ch[], int start, int length) {
        if (inTargetContent) {
            sampleBuffer.append(ch, start, length);
        }
    }

    @Override
    public void endElement(String uri, String localName, String qName) {
        if (inTargetContent) {
            sampleContent = sampleBuffer.toString();
            inTargetContent = false;
        }
        inTargetSample = false;
    }

    public String getSampleTitle() {
        return sampleTitle;
    }

    public String getSampleDescription() {
        return sampleDescription;
    }

    public String getSampleContent() {
        return sampleContent;
    }
}

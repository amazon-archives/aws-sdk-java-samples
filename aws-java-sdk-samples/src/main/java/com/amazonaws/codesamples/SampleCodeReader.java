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

import java.io.InputStream;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import com.amazonaws.codesamples.util.ValidationUtils;
import com.amazonaws.codesamples.exception.SampleRetrievalException;

/**
 * Reads code samples from a source directory containing processed sample code
 * XML files.
 */
public class SampleCodeReader {

    private static final SAXParserFactory parserFactory = SAXParserFactory
            .newInstance();

    private final String sampleDir;
    private final SAXParser saxParser;

    /**
     * Ctor.
     *
     * @param sampleDir
     *            directory containing code sample XML files
     * @throws Exception
     *             if SAX Parser cannot be initialized
     */
    public SampleCodeReader(final String sampleDir) throws Exception {
        this.sampleDir = ValidationUtils.assertInputNotEmpty(sampleDir,
                "Sample directory must be provided");
        this.saxParser = parserFactory.newSAXParser();
    }

    /**
     * Retrieves a specified code sample.
     *
     * @param serviceName
     *            service sample belongs to
     * @param sampleId
     *            unique id of sample
     * @return retrieved code sample or null if sample does not exist
     *         or is incomplete
     * @throws SampleRetrievalException
     *             if sample cannot be retrieved due to an error
     */
    public RetrievedCodeSample readSample(final String serviceName,
            final String sampleId) throws SampleRetrievalException {
        ValidationUtils.assertInputNotEmpty(serviceName,
                "Sample service name must be provided");
        ValidationUtils.assertInputNotEmpty(sampleId,
                "Sample ID must be provided");

        String targetSampleFile = String.format("/%s/%s.xml", sampleDir,
                serviceName);
        InputStream in = getClass().getResourceAsStream(targetSampleFile);

        if (in == null) {
            // sample file does not exist.  typically this means that this service
            // does not yet have any samples.  quietly handle this case.
            return null;
        }

        SampleCodeXMLParser parser = new SampleCodeXMLParser(sampleId);
        try {
            saxParser.parse(in, parser);
        } catch (Exception e) {
            throw new SampleRetrievalException("Could not parse sample",
                    serviceName, sampleId, e);
        }

        String sampleContent = parser.getSampleContent();
        String sampleTitle = parser.getSampleTitle();
        String sampleDescription = parser.getSampleDescription();
        
        if (ValidationUtils.areNullOrEmpty(sampleContent, sampleTitle,
                sampleDescription)) {
            // quietly handle missing or incomplete samples
            return null;
        }

        return new RetrievedCodeSample(sampleId, serviceName,
                sampleTitle, sampleDescription, sampleContent);
    }

}

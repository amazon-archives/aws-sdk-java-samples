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

import com.amazonaws.codesamples.util.SampleHTMLTemplates;
import com.sun.javadoc.Tag;
import com.sun.tools.doclets.Taglet;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import java.net.URL;
import java.net.URLClassLoader;

/**
 * Taglet that retrieves and injects sample code blocks into sections of
 * Javadocs marked with @sample tags.  Samples are retrieved from an
 * intermediate XML format, formatted into HTML blocks, and inserted
 * into the final Javadoc.
 *
 * Example tag: @sample S3.FooSample
 */
public class SampleCodeTaglet implements Taglet {

    private static final String SAMPLE_TAG = "sample";
    private static final String SAMPLES_DIRECTORY = "samples";
    private static final Pattern SAMPLE_TAG_PATTERN = Pattern
            .compile("([^\\s.]+)\\.([^\\s.]+)$");

    private SampleCodeReader reader;

    public SampleCodeTaglet() {
        try {
            reader = new SampleCodeReader(SAMPLES_DIRECTORY);
        } catch (Exception e) {
            System.err.println("Could not initialize sample reader for taglet"
                    + " processor.  Code samples will not be injected.");
            e.printStackTrace();
        }
    }

    @Override
    public String getName() {
        return SAMPLE_TAG;
    }

    @Override
    public boolean inConstructor() {
        return true;
    }

    @Override
    public boolean inField() {
        return true;
    }

    @Override
    public boolean inMethod() {
        return true;
    }

    @Override
    public boolean inOverview() {
        return true;
    }

    @Override
    public boolean inPackage() {
        return true;
    }

    @Override
    public boolean inType() {
        return true;
    }

    @Override
    public boolean isInlineTag() {
        return false;
    }

    public static void register(final Map<String, Taglet> tagletMap) {
        Taglet taglet = tagletMap.get(SAMPLE_TAG);
        if (taglet != null) {
            tagletMap.remove(SAMPLE_TAG);
        }
        tagletMap.put(SAMPLE_TAG, new SampleCodeTaglet());
    }

    @Override
    public String toString(Tag tag) {
        Tag[] singletonTag = { tag };
        return toString(singletonTag);
    }

    @Override
    public String toString(Tag[] tags) {
        if (tags.length == 0) {
            return null;
        }

        List<RetrievedCodeSample> samples = new ArrayList<RetrievedCodeSample>();

        for (int i = 0; i < tags.length; i++) {
            String tagText = tags[i].text();

            Matcher sampleTagMatcher = SAMPLE_TAG_PATTERN
                    .matcher(tagText);

            if (sampleTagMatcher.find()) {
                String serviceName = sampleTagMatcher.group(1);
                String sampleId = sampleTagMatcher.group(2);

                try {
                    RetrievedCodeSample sample = reader.readSample(serviceName, sampleId);
                    if (sample != null) {
                        samples.add(sample);
                    }
                } catch (Exception e) {
                    System.err.println("Could not parse tag: " + tagText);
                    e.printStackTrace();
                }
            } else {
                System.err.println("Sample tag did not match expected format: "
                        + tagText);
            }
        }

        /*
         * Only render the sample block if we've actually got valid samples
         * to display.
         */
        if (!samples.isEmpty()) {
            return SampleHTMLTemplates.getSampleSectionHTML(samples);
        } else {
            return "";
        }
    }
}

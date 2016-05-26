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

import java.io.File;
import java.io.FileReader;
import java.io.LineNumberReader;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.amazonaws.codesamples.exception.SampleParsingException;
import com.amazonaws.util.ParsingUtils;

/**
 * Parser that parses out marked up samples from source code files. Samples are
 * retained in-memory and returned in a List for additional processing or
 * writing.
 */
public class SamplesParser {

    /*
     * Matching patterns for parsing source files.
     */
    private static final Pattern BEGIN_SAMPLE_PATTERN = Pattern
            .compile("//\\s*BEGIN_SAMPLE:([^.]+)\\.([^.]+)$");
    private static final Pattern END_SAMPLE_PATTERN = Pattern
            .compile("//\\s*END_SAMPLE");

    private static final Pattern BEGIN_HIDDEN_PATTERN = Pattern
            .compile("//\\s*BEGIN_HIDDEN");
    private static final Pattern END_HIDDEN_PATTERN = Pattern
            .compile("//\\s*END_HIDDEN");

    private static final Pattern TITLE_PATTERN = Pattern
            .compile("//\\s*TITLE:(.*)$");
    private static final Pattern DESCRIPTION_PATTERN = Pattern
            .compile("//\\s*DESCRIPTION:(.*)$");

    /**
     * Returns a list of samples parsed from input files.
     *
     * @param files
     *            input files
     * @param targetExtension
     *            extension of files to parse
     * @return list of samples
     * @throws Exception
     *             if an error occurs while getting samples
     */
    public static List<CodeSample> getSamples(final File[] files,
            final String targetExtension) throws Exception {
        if (files == null) {
            throw new SampleParsingException(
                    "Must provide a non-null set of files to parse");
        }

        List<CodeSample> samples = new ArrayList<CodeSample>();
        parseFiles(files, targetExtension, samples);
        return samples;
    }

    /**
     * Returns a list of samples parsed from a file.
     *
     * @param file
     *            input file
     * @return list of samples
     * @throws Exception
     *             if an error occurs while getting samples
     */
    public static List<CodeSample> getSamples(final File file)
            throws Exception {
        if (file == null || !file.exists()) {
            throw new SampleParsingException(
                    "Must provide a valid existing file to parse");
        }

        List<CodeSample> samples = new ArrayList<CodeSample>();
        parseFile(file, samples);
        return samples;
    }

    private static void parseFiles(final File[] files,
            final String targetExtension, final List<CodeSample> samples)
            throws Exception {
        for (File file : files) {
            if (file.isDirectory()) {
                parseFiles(file.listFiles(), targetExtension, samples);
            } else {
                if (ParsingUtils.fileMatchesExtension(file, targetExtension)) {
                    parseFile(file, samples);
                }
            }
        }
    }

    private static void parseFile(final File sourceFile,
            List<CodeSample> samples) throws Exception {
        String line = null;

        CodeSample curSample = null;
        boolean inSample = false;
        boolean hidden = false;

        LineNumberReader reader = null;
        try {
            reader = new LineNumberReader(new FileReader(sourceFile));

            while ((line = reader.readLine()) != null) {
                String trimmedLine = line.trim();
                Matcher beginSampleMatcher = BEGIN_SAMPLE_PATTERN
                        .matcher(trimmedLine);
                Matcher endSampleMatcher = END_SAMPLE_PATTERN
                        .matcher(trimmedLine);

                Matcher beginHiddenMatcher = BEGIN_HIDDEN_PATTERN
                        .matcher(trimmedLine);
                Matcher endHiddenMatcher = END_HIDDEN_PATTERN
                        .matcher(trimmedLine);

                Matcher titleMatcher = TITLE_PATTERN
                        .matcher(trimmedLine);
                Matcher descriptionMatcher = DESCRIPTION_PATTERN
                        .matcher(trimmedLine);

                if (beginSampleMatcher.find()) {
                    if (inSample) {
                        throw new SampleParsingException(
                                "Found a BEGIN_SAMPLE tag while already within a sample block",
                                sourceFile, reader.getLineNumber());
                    }
                    inSample = true;
                    String serviceName = beginSampleMatcher.group(1);
                    String sampleId = beginSampleMatcher.group(2);
                    curSample = new CodeSample(sampleId, serviceName);
                } else if (endSampleMatcher.find()) {
                    if (inSample) {
                        ParsingUtils.assertFieldHasContent("title",
                                curSample.getTitle());
                        ParsingUtils.assertFieldHasContent("description",
                                curSample.getDescription());
                        ParsingUtils.assertFieldHasContent("content",
                                curSample.getFormattedCode());

                        if (hidden) {
                            throw new SampleParsingException(
                                    "Sample was still hidden when END_SAMPLE tag was encountered",
                                    sourceFile, reader.getLineNumber());
                        }

                        samples.add(curSample);
                        inSample = false;
                    } else {
                        throw new SampleParsingException(
                                "Found a END_SAMPLE tag while not within a sample block",
                                sourceFile, reader.getLineNumber());
                    }
                } else if (beginHiddenMatcher.find()) {
                    if (hidden == true) {
                        throw new SampleParsingException(
                                "Found a BEGIN_HIDDEN tag while already within a hidden block",
                                sourceFile, reader.getLineNumber());
                    }

                    hidden = true;
                } else if (endHiddenMatcher.find()) {
                    if (hidden == false) {
                        throw new SampleParsingException(
                                "Found a END_HIDDEN tag while not within a hidden block",
                                sourceFile, reader.getLineNumber());
                    }

                    hidden = false;
                } else if (titleMatcher.find()) {
                    if (!inSample) {
                        throw new SampleParsingException(
                                "Found a TITLE tag while not within a sample block",
                                sourceFile, reader.getLineNumber());
                    }
                    curSample.setTitle(titleMatcher.group(1));
                } else if (descriptionMatcher.find()) {
                    if (!inSample) {
                        throw new SampleParsingException(
                                "Found a DESCRIPTION tag while not within a sample block",
                                sourceFile, reader.getLineNumber());
                    }
                    curSample.setDescription(descriptionMatcher.group(1));
                } else if (inSample && !hidden) {
                    curSample.appendLine(line);
                }
            }

            if (inSample) {
                throw new SampleParsingException(
                        "Hit EOF with sample still open: "
                                + curSample.getId(), sourceFile,
                        reader.getLineNumber());
            }
        } finally {
            if (reader != null) {
                reader.close();
            }
        }
    }
}

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
import java.util.List;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;

/**
 * Maven mojo that is responsible for scanning input source files and extracting
 * sections marked up as code samples. Output are generated XML files containing
 * the samples.
 */
@Mojo(name = "ExtractSamples")
public class SampleCodeExtractor extends AbstractMojo {

    @Parameter(property = "sampleSubDir")
    private String sampleSubDir;

    @Parameter(property = "sourceExtension")
    private String sourceExtension;

    @Parameter(defaultValue = "${project.build.sourceDirectory}")
    private String projectSourceDir;

    @Parameter(defaultValue = "${project.build.outputDirectory}")
    private String projectOutputDir;

    /**
     * Plugin entry point.
     */
    public void execute() throws MojoExecutionException, MojoFailureException {
        if (projectSourceDir == null || projectSourceDir.isEmpty()) {
            throw new MojoFailureException(
                    "Unable to determine project source dir!");
        }

        if (projectOutputDir == null || projectOutputDir.isEmpty()) {
            throw new MojoFailureException(
                    "Unable to determine project output dir!");
        }

        if (sampleSubDir == null || sampleSubDir.isEmpty()) {
            throw new MojoFailureException(
                    "Sample sub directory was not provided!");
        }
        File[] sourceFiles = new File(projectSourceDir).listFiles();
        String samplesOutputDir = projectOutputDir + sampleSubDir;

        try {
            List<CodeSample> samples = SamplesParser.getSamples(sourceFiles,
                    sourceExtension);
            SamplesWriter.writeSamples(samples, samplesOutputDir);
        } catch (Exception e) {
            throw new MojoFailureException("Failed to extract samples", e);
        }
    }
}

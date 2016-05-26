/*
 * Copyright 2010-2015 Amazon.com, Inc. or its affiliates. All Rights Reserved.
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
package com.amazonaws.samples;

import java.io.IOException;
import org.apache.maven.execution.MavenSession;
import org.apache.maven.plugins.annotations.Component;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.BuildPluginManager;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.project.MavenProject;

import com.amazonaws.codegen.model.intermediate.IntermediateModel;
import com.amazonaws.samples.util.IntermediateModelRetriever;

import static org.twdata.maven.mojoexecutor.MojoExecutor.*;

/**
 * Mojo to generate samples classes for JSON snippet samples retrieved from
 * dependencies on the classpath.
 */
@Mojo(name = "GenerateSamples")
public class SampleGenerationRunner extends AbstractMojo {

    @Component
    private MavenProject mavenProject;

    @Component
    private MavenSession mavenSession;

    @Component
    private BuildPluginManager pluginManager;

    @Parameter(defaultValue = "${project.build.sourceDirectory}")
    private String projectSourceDir;

    public void execute() throws MojoExecutionException, MojoFailureException {
        try {
            for (IntermediateModel model : IntermediateModelRetriever
                    .getDependencyIntermediateModels()) {
                if (!model.getExamples().getOperationExamples().isEmpty()) {
                    SamplesGenerator writer = new SamplesGenerator(model);
                    GeneratedSamplesClassWriter.writeSamples(projectSourceDir + "/samples", model
                            .getMetadata().getSyncInterface(), writer
                            .getGeneratedSamples());
                }
            }

            executeMojo(
                    plugin(
                        groupId("com.amazonaws"),
                        artifactId("aws-java-sdk-sample-extractor"),
                        version("LATEST")
                    ),
                    goal("ExtractSamples"),
                    configuration(
                            element("sourceExtension", "java"),
                            element("sampleSubDir", "/samples")
                    ),
                    executionEnvironment(
                        mavenProject,
                        mavenSession,
                        pluginManager));

        } catch (IOException e) {
            throw new MojoFailureException("Failed to generate samples", e);
        }
    }
}

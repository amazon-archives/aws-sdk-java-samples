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

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import com.amazonaws.samples.exception.SampleGenerationException;

/**
 * Utility to write contents of a generated samples class to disk.
 */
public class GeneratedSamplesClassWriter {

    /**
     * Writes the contents of a generated samples class to disk.
     *
     * @param outputDirLocation
     *            directory to write to
     * @param serviceName
     *            name of service
     * @param samples
     *            generated sample class contents
     */
    public static void writeSamples(String outputDirLocation, final String serviceName, final String samples) {
        if (!outputDirLocation.endsWith("/")) {
            outputDirLocation += "/";
        }

        File outputDir = new File(outputDirLocation);
        if (!outputDir.exists()) {
            outputDir.mkdirs();
        }

        try {
            String filename = String.format("%sGeneratedSamples.java", serviceName.replaceAll("\\s+", ""));
            Files.write(Paths.get(outputDirLocation + filename), samples.getBytes());
        } catch (IOException e) {
            throw new SampleGenerationException("Failed to write sample class", e);
        }
    }

}

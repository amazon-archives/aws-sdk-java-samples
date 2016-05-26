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
package com.amazonaws.samples.util;

import java.io.IOException;
import java.io.InputStream;
import java.net.JarURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import com.amazonaws.codegen.model.intermediate.IntermediateModel;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Uses Jackson to materialize a serialized intermediate model.
 */
public class IntermediateModelRetriever {

    /**
     * Returns a list of materialized intermediate models from SDK dependencies
     * on the classpath.
     *
     * @return list of materialized intermediate models
     * @throws IOException
     *             if intermediate models cannot be retrieved
     */
    public static List<IntermediateModel> getDependencyIntermediateModels()
            throws IOException {
        List<IntermediateModel> intermediateModels = new ArrayList<>();
        Enumeration<URL> en = IntermediateModelRetriever.class.getClassLoader().getResources(
                "META-INF");

        while (en.hasMoreElements()) {
            URL url = en.nextElement();
            JarURLConnection urlcon = (JarURLConnection) (url.openConnection());
            try (JarFile jar = urlcon.getJarFile()) {
                Enumeration<JarEntry> entries = jar.entries();
                while (entries.hasMoreElements()) {
                    JarEntry entry = entries.nextElement();
                    if (entry.getName().endsWith("-intermediate.json")) {
                        intermediateModels.add(deserializeIntermediateModel(jar
                                .getInputStream(entry)));
                    }
                }
            }
        }
        return intermediateModels;
    }

    /**
     * Retrieves a serialized intermediate model.
     *
     * @param in
     *            stream containing JSON serialized intermediate model
     * @return materialized intermediate model POJO
     * @throws IOException
     *             if intermediate model cannot be read or materialized
     */
    public static IntermediateModel deserializeIntermediateModel(
            final InputStream in) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        return mapper.readValue(in, IntermediateModel.class);
    }

}

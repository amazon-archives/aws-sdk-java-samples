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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 * Sample writer which takes samples and writes them out to XML files.
 */
public class SamplesWriter {

    /**
     * Output XML constants.
     */
    static final String ROOT_ELEMENT = "codeSamples";
    static final String SAMPLE_ELEMENT = "sample";
    static final String CONTENT_ELEMENT = "content";
    static final String ID_ATTRIBUTE = "id";
    static final String TITLE_ATTRIBUTE = "title";
    static final String DESC_ATTRIBUTE = "description";

    /**
     * Writes samples. Samples are split up by service, injected into a service
     * specific DOM, and finally written out to XML files split up by service.
     * Sample DOMs are held in memory until being written to file.
     *
     * @param samples
     *            list of samples to write
     * @param outputDir
     *            sample output directory
     * @throws Exception
     *             if an error occurs while writing samples
     */
    public static void writeSamples(final List<CodeSample> samples,
            final String outputDir) throws Exception {
        Map<String, Document> serviceDocMap = generateSampleDocuments(samples);

        new File(outputDir).mkdirs();

        TransformerFactory transformerFactory = TransformerFactory
                .newInstance();
        Transformer transformer = transformerFactory.newTransformer();
        transformer.setOutputProperty(OutputKeys.INDENT, "yes");

        for (Entry<String, Document> entry : serviceDocMap.entrySet()) {
            DOMSource source = new DOMSource(entry.getValue());
            File outputFile = new File(String.format("%s/%s.xml", outputDir,
                    entry.getKey()));
            StreamResult result = new StreamResult(outputFile);
            transformer.transform(source, result);
        }
    }

    static Map<String, Document> generateSampleDocuments(
            final List<CodeSample> samples) throws Exception {
        Map<String, Document> serviceDocMap = new HashMap<String, Document>();

        for (CodeSample sample : samples) {
            String sampleServiceName = sample.getServiceName();

            if (!serviceDocMap.containsKey(sampleServiceName)) {
                serviceDocMap.put(sampleServiceName, createSampleDoc());
            }
            Document doc = serviceDocMap.get(sampleServiceName);
            appendSampleToDoc(sample, doc);
        }

        return serviceDocMap;
    }

    private static Document createSampleDoc()
            throws ParserConfigurationException {
        DocumentBuilderFactory docFactory = DocumentBuilderFactory
                .newInstance();
        DocumentBuilder docBuilder = docFactory.newDocumentBuilder();

        Document doc = docBuilder.newDocument();
        Element rootElement = doc.createElement(ROOT_ELEMENT);
        doc.appendChild(rootElement);

        return doc;
    }

    private static void appendSampleToDoc(final CodeSample sample,
            final Document doc) {
        Element sampleElement = doc.createElement(SAMPLE_ELEMENT);
        sampleElement.setAttribute(DESC_ATTRIBUTE, sample.getDescription());
        sampleElement.setAttribute(TITLE_ATTRIBUTE, sample.getTitle());
        sampleElement.setAttribute(ID_ATTRIBUTE, sample.getId());

        Element codeElement = doc.createElement(CONTENT_ELEMENT);
        codeElement.appendChild(doc.createTextNode(sample.getFormattedCode()));
        sampleElement.appendChild(codeElement);

        Element rootElement = (Element) doc.getElementsByTagName(ROOT_ELEMENT)
                .item(0);
        rootElement.appendChild(sampleElement);
    }
}

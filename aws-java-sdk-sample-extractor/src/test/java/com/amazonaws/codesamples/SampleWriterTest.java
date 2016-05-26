package com.amazonaws.codesamples;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.hamcrest.Matchers.hasKey;
import static org.hamcrest.MatcherAssert.assertThat;
import static com.amazonaws.codesamples.SampleExtractorTestUtils.getSampleFile;

import java.io.File;
import java.util.List;
import java.util.Map;

import org.junit.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import static com.amazonaws.codesamples.SamplesWriter.ROOT_ELEMENT;
import static com.amazonaws.codesamples.SamplesWriter.SAMPLE_ELEMENT;
import static com.amazonaws.codesamples.SamplesWriter.ID_ATTRIBUTE;
import static com.amazonaws.codesamples.SamplesWriter.TITLE_ATTRIBUTE;
import static com.amazonaws.codesamples.SamplesWriter.DESC_ATTRIBUTE;
import static com.amazonaws.codesamples.SamplesWriter.CONTENT_ELEMENT;

public class SampleWriterTest {

    @Test
    public void generateAndRetrieveSamplesFunctionalTest() throws Exception {
        File sampleFile = getSampleFile("TestSamples.java");
        List<CodeSample> samples = SamplesParser.getSamples(sampleFile);

        Map<String, Document> sampleDocuments = SamplesWriter
                .generateSampleDocuments(samples);
        assertNotNull(sampleDocuments);
        assertEquals(sampleDocuments.size(), 3);

        assertThat(sampleDocuments, hasKey("Test1"));
        assertThat(sampleDocuments, hasKey("Test2"));
        assertThat(sampleDocuments, hasKey("Test3"));

        Document sampleDocument = sampleDocuments.get("Test1");
        Element rootElement = sampleDocument.getDocumentElement();
        assertNotNull(rootElement);
        assertEquals(rootElement.getNodeName(), ROOT_ELEMENT);

        NodeList sampleNodes = rootElement.getChildNodes();
        assertNotNull(sampleNodes);
        assertEquals(sampleNodes.getLength(), 1);

        Node sampleNode = sampleNodes.item(0);
        assertEquals(sampleNode.getNodeName(), SAMPLE_ELEMENT);
        NamedNodeMap map = sampleNode.getAttributes();
        assertNotNull(map);

        Node idAttr = map.getNamedItem(ID_ATTRIBUTE);
        assertEquals(idAttr.getNodeValue(), "Sample1");

        Node titleAttr = map.getNamedItem(TITLE_ATTRIBUTE);
        assertEquals(titleAttr.getNodeValue(), "First sample");

        Node descAttr = map.getNamedItem(DESC_ATTRIBUTE);
        assertEquals(descAttr.getNodeValue(), "The first sample");

        NodeList sampleChildNodes = sampleNode.getChildNodes();
        assertNotNull(sampleChildNodes);
        assertEquals(sampleChildNodes.getLength(), 1);

        Node contentNode = sampleChildNodes.item(0);
        assertEquals(contentNode.getNodeName(), CONTENT_ELEMENT);
        assertEquals(contentNode.getTextContent(), "String foo = \"foo!\";");
    }

}

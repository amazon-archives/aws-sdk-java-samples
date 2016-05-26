package com.amazonaws.codesamples;

import static org.junit.Assert.assertEquals;
import static com.amazonaws.codesamples.SampleExtractorTestUtils.getSampleFile;

import java.io.File;
import java.util.List;

import org.junit.Test;

import com.amazonaws.codesamples.exception.SampleParsingException;

public class SamplesParserTest {

    @Test
    public void parseSamples() throws Exception {
        File sampleFile = getSampleFile("TestSamples.java");
        List<CodeSample> samples = SamplesParser.getSamples(sampleFile);
        assertEquals(samples.size(), 3);

        CodeSample firstSample = samples.get(0);
        assertEquals(firstSample.getId(), "Sample1");
        assertEquals(firstSample.getServiceName(), "Test1");
        assertEquals(firstSample.getTitle(), "First sample");
        assertEquals(firstSample.getDescription(), "The first sample");
        assertEquals(firstSample.getFormattedCode(), "String foo = \"foo!\";");

        CodeSample secondSample = samples.get(1);
        assertEquals(secondSample.getId(), "Sample2");
        assertEquals(secondSample.getServiceName(), "Test2");
        assertEquals(secondSample.getTitle(), "Second sample");
        assertEquals(secondSample.getDescription(), "The second sample");
        assertEquals(secondSample.getFormattedCode(),
                "String bar = \"bar!\";\nString baz = \"baz!\";");

        CodeSample thirdSample = samples.get(2);
        assertEquals(thirdSample.getId(), "Sample3");
        assertEquals(thirdSample.getServiceName(), "Test3");
        assertEquals(thirdSample.getTitle(), "Third sample");
        assertEquals(thirdSample.getDescription(), "The third sample");
        assertEquals(thirdSample.getFormattedCode(),
                "String visible = \"this should be visible\";");
    }

    @Test
    public void fileNoSamples() throws Exception {
        File sampleFile = getSampleFile("EmptySamples.java");
        List<CodeSample> samples = SamplesParser.getSamples(sampleFile);
        assertEquals(samples.size(), 0);
    }

    @Test(expected = SampleParsingException.class)
    public void sampleBadStartTag() throws Exception {
        File sampleFile = getSampleFile("BadStartTagSample.java");
        SamplesParser.getSamples(sampleFile);
    }

    @Test(expected = SampleParsingException.class)
    public void sampleBadEndTag() throws Exception {
        File sampleFile = getSampleFile("BadEndTagSample.java");
        SamplesParser.getSamples(sampleFile);
    }

    @Test(expected = SampleParsingException.class)
    public void sampleBadBeginHiddenTag() throws Exception {
        File sampleFile = getSampleFile("BadBeginHiddenTagSample.java");
        SamplesParser.getSamples(sampleFile);
    }

    @Test(expected = SampleParsingException.class)
    public void sampleBadEndHiddenTag() throws Exception {
        File sampleFile = getSampleFile("BadEndHiddenTagSample.java");
        SamplesParser.getSamples(sampleFile);
    }

    @Test(expected = SampleParsingException.class)
    public void sampleBadTitleTag() throws Exception {
        File sampleFile = getSampleFile("BadTitleTagSample.java");
        SamplesParser.getSamples(sampleFile);
    }

    @Test(expected = SampleParsingException.class)
    public void sampleBadDescriptionTag() throws Exception {
        File sampleFile = getSampleFile("BadDescriptionTagSample.java");
        SamplesParser.getSamples(sampleFile);
    }

    @Test(expected = SampleParsingException.class)
    public void unclosedSample() throws Exception {
        File sampleFile = getSampleFile("UnclosedSample.java");
        SamplesParser.getSamples(sampleFile);
    }

    @Test(expected = SampleParsingException.class)
    public void sampleNoDescription() throws Exception {
        File sampleFile = getSampleFile("NoDescriptionSample.java");
        SamplesParser.getSamples(sampleFile);
    }

    @Test(expected = SampleParsingException.class)
    public void sampleNoTitle() throws Exception {
        File sampleFile = getSampleFile("NoTitleSample.java");
        SamplesParser.getSamples(sampleFile);
    }

    @Test(expected = SampleParsingException.class)
    public void sampleNoContent() throws Exception {
        File sampleFile = getSampleFile("NoContentSample.java");
        SamplesParser.getSamples(sampleFile);
    }

}

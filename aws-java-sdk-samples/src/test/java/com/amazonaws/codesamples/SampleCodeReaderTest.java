package com.amazonaws.codesamples;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;
import org.junit.Assert;

import com.amazonaws.codesamples.exception.SampleRetrievalException;

public class SampleCodeReaderTest {

    private static final String SAMPLE_ID = "FooSample";
    private static final String SERVICE_NAME = "FooService";

    private SampleCodeReader reader;

    @Before
    public void setUp() throws Exception {
        reader = new SampleCodeReader("testsamples");
    }

    @Test
    public void testRetrieveValidSample() throws Exception {
        RetrievedCodeSample sample = reader.readSample(SERVICE_NAME,
                SAMPLE_ID);
        assertEquals(sample.getId(), SAMPLE_ID);
        assertEquals(sample.getService(), SERVICE_NAME);
        assertEquals(sample.getTitle(), "Foo Sample");
        assertEquals(sample.getDescription(), "A foo sample!");
        assertEquals(sample.getContent(), "String foo = \"foo!\";");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testRetrieveSampleNullService() throws SampleRetrievalException {
        reader.readSample(null, SAMPLE_ID);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testRetrieveSampleEmptyService()
            throws SampleRetrievalException {
        reader.readSample("", SAMPLE_ID);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testRetrieveSampleNullSample() throws SampleRetrievalException {
        reader.readSample(SERVICE_NAME, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testRetrieveSampleEmptySample() throws SampleRetrievalException {
        reader.readSample(SERVICE_NAME, "");
    }

    @Test
    public void testRetrieveNonExistantSample() throws SampleRetrievalException {
        Assert.assertNull(reader.readSample(SERVICE_NAME, "BogusSample"));
    }

    @Test
    public void testRetrieveSampleWithMissingTitle()
            throws SampleRetrievalException {
        Assert.assertNull(reader.readSample(SERVICE_NAME, "MissingTitleSample"));
    }

    @Test
    public void testRetrieveSampleWithEmptyTitle()
            throws SampleRetrievalException {
        Assert.assertNull(reader.readSample(SERVICE_NAME, "EmptyTitleSample"));
    }

    @Test
    public void testRetrieveSampleWithMissingDescription()
            throws SampleRetrievalException {
        Assert.assertNull(reader.readSample(SERVICE_NAME, "MissingDescriptionSample"));
    }

    @Test
    public void testRetrieveSampleWithEmptyDescription()
            throws SampleRetrievalException {
        Assert.assertNull(reader.readSample(SERVICE_NAME, "EmptyDescriptionSample"));
    }

    @Test
    public void testRetrieveSampleWithMissingContent()
            throws SampleRetrievalException {
        Assert.assertNull(reader.readSample(SERVICE_NAME, "MissingContentSample"));
    }

    @Test
    public void testRetrieveSampleWithEmptyContent()
            throws SampleRetrievalException {
        Assert.assertNull(reader.readSample(SERVICE_NAME, "EmptyContentSample"));
    }
}

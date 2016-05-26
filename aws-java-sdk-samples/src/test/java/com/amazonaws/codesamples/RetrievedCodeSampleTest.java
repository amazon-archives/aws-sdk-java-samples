package com.amazonaws.codesamples;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class RetrievedCodeSampleTest {

    private static final String SAMPLE_ID = "FooSample";
    private static final String SERVICE_NAME = "FooService";
    private static final String TITLE = "FooTitle";
    private static final String DESCRIPTION = "FooDesc";
    private static final String CONTENT = "FooContent";

    @Test
    public void testRetrievedCodeSample() {
        RetrievedCodeSample sample = new RetrievedCodeSample(SAMPLE_ID,
                SERVICE_NAME, TITLE, DESCRIPTION, CONTENT);
        assertEquals(SAMPLE_ID, sample.getId());
        assertEquals(SERVICE_NAME, sample.getService());
        assertEquals(TITLE, sample.getTitle());
        assertEquals(DESCRIPTION, sample.getDescription());
        assertEquals(CONTENT, sample.getContent());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testRetrievedCodeSampleNullId() {
        new RetrievedCodeSample(null, SERVICE_NAME, TITLE, DESCRIPTION, CONTENT);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testRetrievedCodeSampleEmptyId() {
        new RetrievedCodeSample("", SERVICE_NAME, TITLE, DESCRIPTION, CONTENT);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testRetrievedCodeSampleNullServiceName() {
        new RetrievedCodeSample(SAMPLE_ID, null, TITLE, DESCRIPTION, CONTENT);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testRetrievedCodeSampleEmptyServiceName() {
        new RetrievedCodeSample(SAMPLE_ID, "", TITLE, DESCRIPTION, CONTENT);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testRetrievedCodeSampleNullTitle() {
        new RetrievedCodeSample(SAMPLE_ID, SERVICE_NAME, null, DESCRIPTION, CONTENT);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testRetrievedCodeSampleEmptyTitle() {
        new RetrievedCodeSample(SAMPLE_ID, SERVICE_NAME, "", DESCRIPTION, CONTENT);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testRetrievedCodeSampleNullDescription() {
        new RetrievedCodeSample(SAMPLE_ID, SERVICE_NAME, TITLE, null, CONTENT);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testRetrievedCodeSampleEmptyDescription() {
        new RetrievedCodeSample(SAMPLE_ID, SERVICE_NAME, TITLE, "", CONTENT);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testRetrievedCodeSampleNullContent() {
        new RetrievedCodeSample(SAMPLE_ID, SERVICE_NAME, TITLE, DESCRIPTION, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testRetrievedCodeSampleEmptyContent() {
        new RetrievedCodeSample(SAMPLE_ID, SERVICE_NAME, TITLE, DESCRIPTION, "");
    }
}

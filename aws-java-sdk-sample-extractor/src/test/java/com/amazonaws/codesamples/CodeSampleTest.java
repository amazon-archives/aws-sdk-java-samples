package com.amazonaws.codesamples;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class CodeSampleTest {

    @Test(expected = IllegalArgumentException.class)
    public void codeSampleNullName() {
        new CodeSample(null, "Service");
    }

    @Test(expected = IllegalArgumentException.class)
    public void codeSampleEmptyName() {
        new CodeSample("", "Service");
    }

    @Test(expected = IllegalArgumentException.class)
    public void codeSampleNullService() {
        new CodeSample("Name", null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void codeSampleEmptyService() {
        new CodeSample("Name", "");
    }

    @Test
    public void getId() {
        CodeSample sample = new CodeSample("ID", "Service");
        assertEquals(sample.getId(), "ID");
    }

    @Test
    public void getService() {
        CodeSample sample = new CodeSample("ID", "Service");
        assertEquals(sample.getServiceName(), "Service");
    }

    @Test
    public void setGetTitle() {
        CodeSample sample = new CodeSample("ID", "Service");
        sample.setTitle("SampleTitle");
        assertEquals(sample.getTitle(), "SampleTitle");
    }

    @Test
    public void setGetDescription() {
        CodeSample sample = new CodeSample("ID", "Service");
        sample.setDescription("Description");
        assertEquals(sample.getDescription(), "Description");
    }

    @Test
    public void setGetSampleContent() {
        CodeSample sample = new CodeSample("ID", "Service");
        sample.setTitle("Title");
        sample.setDescription("Description");

        sample.appendLine("One");
        sample.appendLine("Two");
        sample.appendLine("Three");

        assertEquals(sample.getFormattedCode(), "One\nTwo\nThree");
    }

}

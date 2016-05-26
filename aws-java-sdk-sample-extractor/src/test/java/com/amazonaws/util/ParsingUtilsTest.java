package com.amazonaws.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static com.amazonaws.codesamples.SampleExtractorTestUtils.getSampleFile;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

public class ParsingUtilsTest {

    @Test
    public void fileMatchesExtension() throws Exception {
        File sampleFile = getSampleFile("EmptySamples.java");
        assertTrue(ParsingUtils.fileMatchesExtension(sampleFile, "java"));
    }

    @Test
    public void fileDoesNotMatchExtension() throws Exception {
        File sampleFile = getSampleFile("BadExtension.cpp");
        assertFalse(ParsingUtils.fileMatchesExtension(sampleFile, "java"));
    }

    @Test
    public void getLeadingWhitespace() {
        assertEquals(ParsingUtils.getLeadingWhitespace("ZERO"), 0);
        assertEquals(ParsingUtils.getLeadingWhitespace("   THREE"), 3);
    }

    @Test
    public void trimLeadingWhitespace() {
        String foo = "   FOO";
        assertEquals(ParsingUtils.trimLeadingWhitespace(foo, 1), "  FOO");
        assertEquals(ParsingUtils.trimLeadingWhitespace(foo, 3), "FOO");
    }

    @Test
    public void getMinWhitespace() {
        List<String> lines = new ArrayList<String>();
        lines.add("  FOO");
        lines.add(" BAR");
        lines.add("     BAZ");
        assertEquals(ParsingUtils.getMinWhitespace(lines), 1);
    }

    @Test
    public void formatSampleText() {
        List<String> lines = new ArrayList<String>();
        lines.add(" ONE");
        lines.add("  TWO");
        lines.add(" THREE");

        assertEquals(ParsingUtils.formatSampleText(lines, 1),
                "ONE\n TWO\nTHREE");
    }
}

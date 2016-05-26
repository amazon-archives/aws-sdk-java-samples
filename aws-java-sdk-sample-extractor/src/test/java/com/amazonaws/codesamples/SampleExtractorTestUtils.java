package com.amazonaws.codesamples;

import java.io.File;
import java.net.URISyntaxException;
import java.net.URL;

public final class SampleExtractorTestUtils {

    public static File getSampleFile(final String sampleFile)
            throws URISyntaxException {
        URL url = SampleExtractorTestUtils.class.getResource("/testsamples/"
                + sampleFile);
        return new File(url.toURI());
    }

}

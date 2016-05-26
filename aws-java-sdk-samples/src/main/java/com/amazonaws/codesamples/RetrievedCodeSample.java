package com.amazonaws.codesamples;

import com.amazonaws.codesamples.util.ValidationUtils;

/**
 * Represents a code sample retrieved from an XML code samples file.
 */
public class RetrievedCodeSample {

    private final String id;
    private final String service;
    private final String title;
    private final String description;
    private final String content;

    /**
     * Ctor.
     *
     * @param id
     *            sample id
     * @param service
     *            name of service sample associated with
     * @param title
     *            title of the sample
     * @param description
     *            description of the sample
     * @param content
     *            code sample content
     */
    public RetrievedCodeSample(final String id, final String service,
            final String title, final String description, final String content) {
        this.id = ValidationUtils.assertInputNotEmpty(id,
                "Sample ID must be provided");
        this.service = ValidationUtils.assertInputNotEmpty(service,
                "Sample service name must be provided");
        this.title = ValidationUtils.assertInputNotEmpty(title,
                "Sample title must be provided");
        this.description = ValidationUtils.assertInputNotEmpty(description,
                "Sample description must be provided");
        this.content = ValidationUtils.assertInputNotEmpty(content,
                "Sample content must be provided");
    }

    /**
     * Returns the id of the sample.
     *
     * @return id of the sample
     */
    public String getId() {
        return id;
    }

    /**
     * Returns the name of the service the sample is associated with.
     *
     * @return name of service sample is associated with
     */
    public String getService() {
        return service;
    }

    /**
     * Returns the title of the sample.
     *
     * @return title of the sample
     */
    public String getTitle() {
        return title;
    }

    /**
     * Returns the description of the sample.
     *
     * @return description of the sample
     */
    public String getDescription() {
        return description;
    }

    /**
     * Returns the code sample content
     *
     * @return sample content
     */
    public String getContent() {
        return content;
    }
}

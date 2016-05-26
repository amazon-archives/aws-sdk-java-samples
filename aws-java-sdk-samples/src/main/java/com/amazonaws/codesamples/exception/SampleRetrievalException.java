package com.amazonaws.codesamples.exception;

/**
 * Exception thrown when an error occurs while retrieving a code sample.
 */
public class SampleRetrievalException extends RuntimeException {

    private static final long serialVersionUID = 4855855418438710812L;

    /**
     * Constructs a SampleRetrievalException with the specified message.
     *
     * @param serviceName
     *            name of source service for sample
     * @param sampleId
     *            id of sample being retrieved
     * @param message
     *            exception detail message
     */
    public SampleRetrievalException(final String message,
            final String serviceName, final String sampleId) {
        super(formatExceptionMessage(message, serviceName, sampleId));
    }

    /**
     * Constructs a SampleRetrievalException with the specified message and
     * cause.
     *
     * @param serviceName
     *            name of source service for sample
     * @param sampleId
     *            id of sample being retrieved
     * @param message
     *            exception detail message
     * @param cause
     *            exception cause
     */
    public SampleRetrievalException(final String message,
            final String serviceName, final String sampleId,
            final Throwable cause) {
        super(formatExceptionMessage(message, serviceName, sampleId), cause);
    }

    private static String formatExceptionMessage(final String message,
            final String serviceName, final String sampleId) {
        return String.format("%s (Service Name: %s, Sample ID: %s)", message,
                serviceName, sampleId);
    }
}

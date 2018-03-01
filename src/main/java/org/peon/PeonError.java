package org.peon;

/**
 * A simple error object containing a message and error details intended to be shown to the user
 * in the user interface.
 */
public class PeonError {

    /**
     * The message of the error.
     */
    private String message;
    /**
     * The details of the error.
     */
    private String details;

    /**
     * Creates a new error.
     *
     * @param message The message of the error.
     * @param details The details of the error.
     */
    public PeonError(String message, String details) {
        setMessage(message);
        setDetails(details);
    }

    /**
     * Gets the message of the error.
     *
     * @return The message of the error.
     */
    public String getMessage() {
        return message;
    }
    /**
     * Sets the message of the error.
     *
     * @param message The message of the error.
     */
    public void setMessage(String message) {
        this.message = message;
    }

    /**
     * Gets the details of the error.
     *
     * @return The details of the error.
     */
    public String getDetails() {
        return details;
    }
    /**
     * Sets the details of the error.
     *
     * @param details The details of the error.
     */
    public void setDetails(String details) {
        this.details = details;
    }
}

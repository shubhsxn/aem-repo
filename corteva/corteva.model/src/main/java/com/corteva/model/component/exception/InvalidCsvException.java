package com.corteva.model.component.exception;

/**
 * The is the exception class to capture Invalid CSV Fail Scenario
 *
 * @author Sapient
 */
public class InvalidCsvException extends Exception {

    /**
     * This exception is thrown when Invalid CSV is authored
     *
     * @param message the message
     */
    public InvalidCsvException(String message) {
        super(message);
    }
}

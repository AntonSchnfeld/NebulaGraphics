package com.github.nebula.graphics.globjects.exceptions;

/**
 * Represents an exception that occurs during shader validation.
 * This exception is typically thrown when a shader program fails to validate successfully.
 * @author Anton Schoenfeld
 * @since 07.03.2024
 */
public class ShaderValidationException extends ShaderException {

    /**
     * Constructs a new ShaderValidationException with the specified error message.
     *
     * @param message The error message describing the cause of the validation failure.
     */
    public ShaderValidationException(String message) {
        super(message);
    }

    /**
     * Constructs a new ShaderValidationException with no detail message.
     * This constructor is typically used when the cause of the validation failure is unknown or irrelevant.
     */
    public ShaderValidationException() {
        super();
    }
}
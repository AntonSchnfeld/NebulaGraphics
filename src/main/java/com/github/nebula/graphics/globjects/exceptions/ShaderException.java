package com.github.nebula.graphics.globjects.exceptions;

/**
 * Represents a generic exception that can occur during shader operations.
 * This exception is the base class for more specific shader-related exceptions.
 * @author Anton Schoenfeld
 * @since 07.03.2024
 */
public class ShaderException extends RuntimeException {

    /**
     * Constructs a new ShaderException with the specified detail message.
     *
     * @param msg The detail message describing the specific shader-related error.
     */
    public ShaderException(String msg) {
        super(msg);
    }

    /**
     * Constructs a new ShaderException with no detail message.
     * This constructor is typically used when the cause of the shader-related error is unknown or irrelevant.
     */
    public ShaderException() {
        super();
    }

    /**
     * Constructs a new ShaderException with the specified cause.
     * This constructor is typically used when the shader-related error is caused by another throwable object.
     *
     * @param cause The cause of the shader-related error.
     */
    public ShaderException(Throwable cause) {
        super(cause);
    }
}

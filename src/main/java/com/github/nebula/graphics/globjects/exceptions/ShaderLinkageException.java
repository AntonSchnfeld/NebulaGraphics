package com.github.nebula.graphics.globjects.exceptions;

/**
 * Represents an exception that occurs during shader linkage.
 * This exception is typically thrown when a shader program fails to link successfully.
 *
 * @author Anton Schoenfeld
 * @since 07.03.2024
 */
public class ShaderLinkageException extends ShaderException {

    /**
     * Constructs a new ShaderLinkageException with the specified error message.
     *
     * @param message The error message describing the cause of the linkage failure.
     */
    public ShaderLinkageException(String message) {
        super(message);
    }

    /**
     * Constructs a new ShaderLinkageException with no detail message.
     * This constructor is typically used when the cause of the linkage failure is unknown or irrelevant.
     */
    public ShaderLinkageException() {
        super();
    }
}

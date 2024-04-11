package com.github.nebula.graphics.globjects.exceptions;

/**
 * Represents an exception that occurs during shader compilation.
 * This exception is typically thrown when a shader fails to compile successfully.
 *
 * @author Anton Schoenfeld
 * @since 07.03.2024
 */
public class ShaderCompileException extends ShaderException {

    /**
     * Constructs a new ShaderCompileException with the specified error message.
     *
     * @param message The error message describing the cause of the compilation failure.
     */
    public ShaderCompileException(String message) {
        super(message);
    }

    /**
     * Constructs a new ShaderCompileException with no detail message.
     * This constructor is typically used when the cause of the compilation failure is unknown or irrelevant.
     */
    public ShaderCompileException() {
        super();
    }
}
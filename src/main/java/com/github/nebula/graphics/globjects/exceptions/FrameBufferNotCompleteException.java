package com.github.nebula.graphics.globjects.exceptions;

/**
 * Indicates that a frame buffer is not complete.
 * This exception is typically thrown when the status of a frame buffer object (FBO) is not complete.
 *
 * @author Anton Schoenfeld
 * @since 12.03.2024
 */
public class FrameBufferNotCompleteException extends RuntimeException {

    /**
     * Constructs a new FrameBufferNotCompleteException with no detail message.
     * This constructor is typically used when the cause of the incomplete frame buffer is unknown or irrelevant.
     */
    public FrameBufferNotCompleteException() {
        super();
    }

    /**
     * Constructs a new FrameBufferNotCompleteException with the specified detail message.
     *
     * @param msg The detail message describing the cause of the incomplete frame buffer.
     */
    public FrameBufferNotCompleteException(String msg) {
        super(msg);
    }
}
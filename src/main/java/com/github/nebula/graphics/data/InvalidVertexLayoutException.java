package com.github.nebula.graphics.data;

/**
 * Represents an exception indicating an invalid vertex layout.
 * This exception is typically thrown when a vertex layout does not conform to expected specifications.
 *
 * @author Anton Schoenfeld
 * @since 21.03.2024
 */
public class InvalidVertexLayoutException extends RuntimeException {

    /**
     * Constructs a new InvalidVertexLayoutException with no detail message.
     * This constructor is typically used when the cause of the invalid vertex layout is unknown or irrelevant.
     */
    public InvalidVertexLayoutException() {
        super();
    }

    /**
     * Constructs a new InvalidVertexLayoutException with the specified detail message.
     *
     * @param msg The detail message describing the cause of the invalid vertex layout.
     */
    public InvalidVertexLayoutException(String msg) {
        super(msg);
    }
}

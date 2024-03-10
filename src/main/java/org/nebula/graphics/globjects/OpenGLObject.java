package org.nebula.graphics.globjects;

/**
 * The {@code OpenGLObject} class is an abstract base class representing an OpenGL object.
 * It implements the {@link AutoCloseable} interface, allowing for convenient resource cleanup.
 * This class serves as a foundation for specific OpenGL objects by defining common behavior
 * such as binding and unbinding.
 *
 * <p>The main purpose of this class is to encapsulate the OpenGL object's identifier (id) and provide
 * a consistent interface for binding and unbinding operations. It is intended to be extended by classes
 * representing different types of OpenGL objects, such as textures, buffers, or renderbuffers.
 *
 * @author Anton Schoenfeld
 * @since 10.03.2024
 */
public abstract class OpenGLObject implements AutoCloseable {

    /**
     * The OpenGL identifier for the object.
     */
    public final int id;

    /**
     * Constructs an OpenGLObject with the specified OpenGL identifier.
     *
     * @param id The OpenGL identifier for the object.
     */
    public OpenGLObject(final int id) {
        this.id = id;
    }

    /**
     * Binds the OpenGL object, making it the active object for subsequent OpenGL operations.
     */
    public abstract void bind();

    /**
     * Unbinds the OpenGL object, making no object active.
     */
    public abstract void unbind();

    /**
     * Closes the resources associated with the OpenGL object.
     * This method is automatically invoked when used in a try-with-resources statement.
     */
    @Override
    public abstract void close();
}
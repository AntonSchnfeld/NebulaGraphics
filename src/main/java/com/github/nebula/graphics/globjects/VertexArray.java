package com.github.nebula.graphics.globjects;

import static org.lwjgl.opengl.GL33C.*;

/**
 * The {@code VertexArray} class represents an OpenGL Vertex Array Object (VAO) used for efficient organization
 * and management of vertex attributes in graphics applications. It encapsulates the creation, binding, and
 * destruction of VAOs, providing convenient methods for working with vertex attribute arrays.
 *
 * <p>This class implements the {@link AutoCloseable} interface, allowing for automatic resource cleanup.
 * When an instance is no longer needed, calling the {@link #close()} method deletes the associated VAO.
 *
 * <p>Instances of this class should be used in conjunction with {@link Buffer} and {@link Shader}
 * to define the layout of vertex attributes and efficiently pass data to the GPU.
 *
 * @author Anton Schoenfeld
 * @since 07.03.2025
 */
public class VertexArray extends OpenGLObject {

    /**
     * The currently bound VertexArray, used to avoid redundant OpenGL API calls.
     */
    private static VertexArray current;


    /**
     * Constructs a new VertexArray and generates an OpenGL Vertex Array Object (VAO).
     */
    public VertexArray() {
        super(glGenVertexArrays());
    }

    /**
     * Binds the VertexArray, making it the active VAO for subsequent OpenGL operations.
     */
    public void bind() {
        if (!isBound()) {
            glBindVertexArray(id);
            current = this;
        }
    }

    /**
     * Unbinds the currently bound VertexArray, making no VAO active.
     */
    public void unbind() {
        if (isBound()) {
            glBindVertexArray(0);
        }
    }

    /**
     * Enables the vertex attribute array at the specified position.
     *
     * @param position The position of the vertex attribute array.
     */
    public void enableVertexAttributeArray(int position) {
        bind();
        glEnableVertexAttribArray(position);
    }

    /**
     * Specifies the location and data format of a vertex attribute in the VAO and links
     * that attribute to a buffer.
     *
     * @param buffer   The buffer which will be linked to the attribute.
     * @param index    The attribute index.
     * @param size     The number of components per attribute.
     * @param dataType The data type of each component.
     * @param stride   The byte offset between consecutive generic vertex attributes.
     * @param pointer  The offset of the first component of the first generic vertex attribute.
     */
    public void vertexAttribPointer(Buffer buffer, int index, int size, Buffer.Datatype dataType, int stride, int pointer) {
        bind();
        buffer.bind();
        glVertexAttribPointer(index, size, dataType.getGlConstant(), false, stride, pointer);
        enableVertexAttributeArray(index);
    }

    /**
     * Specifies the location and data format of a vertex attribute in the VAO.
     *
     * @param index    The attribute index.
     * @param size     The number of components per attribute.
     * @param dataType The data type of each component.
     * @param stride   The byte offset between consecutive generic vertex attributes.
     * @param pointer  The offset of the first component of the first generic vertex attribute.
     */
    public void vertexAttribPointer(int index, int size, Buffer.Datatype dataType, int stride, int pointer) {
        bind();
        glVertexAttribPointer(index, size, dataType.getGlConstant(), false, stride, pointer);
        enableVertexAttributeArray(index);
    }

    /**
     * Disables the vertex attribute array at the specified position.
     *
     * @param position The position of the vertex attribute array.
     */
    public void disableVertexAttribArray(int position) {
        bind();
        glDisableVertexAttribArray(position);
    }

    /**
     * Checks if this VertexArray is currently bound.
     *
     * @return {@code true} if this VertexArray is bound; {@code false} otherwise.
     */
    private boolean isBound() {
        return current == this;
    }

    /**
     * Deletes the OpenGL Vertex Array Object associated with this VertexArray.
     */
    @Override
    public void close() {
        glDeleteVertexArrays(id);
    }
}

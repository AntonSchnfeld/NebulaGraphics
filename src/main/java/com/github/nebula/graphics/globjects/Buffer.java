package com.github.nebula.graphics.globjects;

import com.github.nebula.graphics.data.GLDataType;
import lombok.Getter;

import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import static org.lwjgl.opengl.GL33C.*;

/**
 * <br>
 * <h2>Buffer</h2>
 * <br>
 * The Buffer class provides a convenient abstraction for managing OpenGL buffer objects, such as Vertex Buffer Objects (VBOs)
 * and Element Buffer Objects (EBOs). It includes methods for binding, unbinding, and storing data in the buffer.
 * This class also implements the IDisposable interface for efficient resource cleanup.
 *
 * @author Anton Schoenfeld
 */
public class Buffer extends OpenGLObject {
    private final int bufferType;

    /**
     * Constructs a Buffer object with the specified buffer type (e.g., GL_ARRAY_BUFFER, GL_ELEMENT_ARRAY_BUFFER).
     *
     * @param type The OpenGL buffer type.
     */
    public Buffer(int type) {
        super(glGenBuffers());
        this.bufferType = type;
    }

    /**
     * Binds the buffer, making it the current buffer of the specified type.
     */
    public void bind() {
        glBindBuffer(bufferType, id);
    }

    /**
     * Unbinds the buffer, switching back to the default buffer for the specified type.
     */
    public void unbind() {
        glBindBuffer(bufferType, 0);
    }

    /**
     * Stores the specified float array data in the buffer with the given usage pattern.
     *
     * @param data  The float array data to be stored in the buffer.
     * @param usage The buffer usage pattern indicating how the data will be accessed and modified.
     */
    public void data(float[] data, int usage) {
        bind();
        glBufferData(bufferType, data, usage);
    }

    /**
     * Stores the specified FloatBuffer data in the buffer with the given usage pattern.
     *
     * @param data  The FloatBuffer data to be stored in the buffer.
     * @param usage The buffer usage pattern indicating how the data will be accessed and modified.
     */
    public void data(FloatBuffer data, int usage) {
        bind();
        glBufferData(bufferType, data, usage);
    }

    /**
     * Stores the specified int array data in the buffer with the given usage pattern.
     *
     * @param data  The int array data to be stored in the buffer.
     * @param usage The buffer usage pattern indicating how the data will be accessed and modified.
     */
    public void data(int[] data, int usage) {
        bind();
        glBufferData(bufferType, data, usage);
    }

    /**
     * Stores the specified IntBuffer data in the buffer with the given usage pattern.
     *
     * @param data  The IntBuffer data to be stored in the buffer.
     * @param usage The buffer usage pattern indicating how the data will be accessed and modified.
     */
    public void data(IntBuffer data, int usage) {
        bind();
        glBufferData(bufferType, data, usage);
    }

    public void data(long data, int usage, GLDataType dataType) {
        bind();
        glBufferData(bufferType, data * dataType.byteSize, usage);
    }

    public void subData(float[] data, long offset) {
        bind();
        glBufferSubData(bufferType, offset * Float.BYTES, data);
    }

    public void subData(FloatBuffer data, long offset) {
        bind();
        glBufferSubData(bufferType, offset * Float.BYTES, data);
    }

    public void subData(int[] data, long offset) {
        bind();
        glBufferSubData(bufferType, offset * Integer.BYTES, data);
    }

    public void subData(IntBuffer data, long offset) {
        bind();
        glBufferSubData(bufferType, offset * Integer.BYTES, data);
    }

    public ByteBuffer map(int readPolicy) {
        bind();
        return glMapBuffer(bufferType, readPolicy);
    }

    public ByteBuffer mapRange(int readPolicy, long offset, int length) {
        bind();
        return glMapBufferRange(bufferType, offset, length, readPolicy);
    }

    public ByteBuffer mapRange(int readPolicy, long offset, int length, ByteBuffer buffer) {
        bind();
        return glMapBufferRange(bufferType, offset, length, readPolicy, buffer);
    }

    /**
     * Disposes of the buffer, releasing associated OpenGL resources.
     * This method should be called when the buffer is no longer needed to avoid memory leaks.
     */
    @Override
    public void close() {
        glDeleteBuffers(id);
    }
}
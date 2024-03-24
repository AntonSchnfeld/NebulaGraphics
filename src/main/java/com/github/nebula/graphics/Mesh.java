package com.github.nebula.graphics;

import com.github.nebula.graphics.globjects.Shader;
import com.github.nebula.graphics.globjects.VertexArray;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;

/**
 * @author Anton Schoenfeld
 * @since 24.03.2024
 */
public interface Mesh extends AutoCloseable {
    FloatBuffer getVerticesRange(long offset, int length, boolean write);
    IntBuffer getIndicesRange(long offset, int length, boolean write);
    FloatBuffer getVertices(boolean write);
    IntBuffer getIndices(boolean write);
    void setVerticesRange(long offset, FloatBuffer buffer);
    void setIndicesRange(long offset, IntBuffer buffer);
    void setVertices(FloatBuffer vertices);
    void setIndices(IntBuffer indices);
    long getVerticesSize();
    long getIndicesSize();
}
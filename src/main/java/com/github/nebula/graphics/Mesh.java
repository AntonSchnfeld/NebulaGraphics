package com.github.nebula.graphics;

import io.reactivex.rxjava3.annotations.NonNull;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;

/**
 * @author Anton Schoenfeld
 * @since 24.03.2024
 */
public interface Mesh extends AutoCloseable {
    CloseableBuffer<FloatBuffer> getVerticesRange(long offset, int length, boolean write);
    CloseableBuffer<IntBuffer> getIndicesRange(long offset, int length, boolean write);
    CloseableBuffer<FloatBuffer> getVertices(boolean write);
    CloseableBuffer<IntBuffer> getIndices(boolean write);
    void setVerticesRange(long offset, @NonNull FloatBuffer buffer);
    void setIndicesRange(long offset, @NonNull IntBuffer buffer);
    void setVertices(@NonNull FloatBuffer vertices);
    void setIndices(@NonNull IntBuffer indices);
    long getVerticesSize();
    long getIndicesSize();
    /**
     * {@inheritDoc}
     */
    void close();
}

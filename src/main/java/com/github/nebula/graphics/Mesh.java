package com.github.nebula.graphics;

import io.reactivex.rxjava3.annotations.NonNull;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;

/**
 * @author Anton Schoenfeld
 * @since 24.03.2024
 */
public abstract class Mesh implements AutoCloseable {
    public boolean dirty;

    public Mesh() {
        dirty = false;
    }

    public abstract CloseableBuffer<FloatBuffer> getVerticesRange(long offset, int length, ReadPolicy readPolicy);

    public abstract CloseableBuffer<IntBuffer> getIndicesRange(long offset, int length, ReadPolicy readPolicy);

    public abstract CloseableBuffer<FloatBuffer> getVertices(ReadPolicy readPolicy);

    public abstract CloseableBuffer<IntBuffer> getIndices(ReadPolicy readPolicy);

    public abstract void setVerticesRange(long offset, @NonNull FloatBuffer buffer);

    public abstract void setIndicesRange(long offset, @NonNull IntBuffer buffer);

    public abstract void setVertices(@NonNull FloatBuffer vertices);

    public abstract void setIndices(@NonNull IntBuffer indices);

    public abstract long getVerticesSize();

    public abstract long getIndicesSize();
    /**
     * {@inheritDoc}
     */
    public abstract void close();
}

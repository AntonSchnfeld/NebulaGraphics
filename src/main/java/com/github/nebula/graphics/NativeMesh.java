package com.github.nebula.graphics;

import com.github.nebula.graphics.util.BufferUtil;
import lombok.Getter;
import lombok.NonNull;
import org.lwjgl.system.MemoryUtil;

import java.nio.Buffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import static org.lwjgl.opengl.GL43C.*;

/**
 * @author Anton Schoenfeld
 * @since 24.03.2024
 */
public class NativeMesh implements Mesh {
    private FloatBuffer vertices;
    private IntBuffer indices;
    private @Getter long verticesSize, indicesSize;

    public NativeMesh() {
        vertices = null;
        indices = null;
        verticesSize = 0;
        indicesSize = 0;
    }

    @Override
    public NativeCloseableBuffer<FloatBuffer> getVerticesRange(long offset, int length, ReadPolicy readPolicy) {
        if (readPolicy.glReadPolicy == GL_READ_WRITE || readPolicy.glReadPolicy == GL_READ_ONLY)
            return new NativeCloseableBuffer<>(vertices.slice((int) offset, length));
        return new NativeCloseableBuffer<>(vertices.slice((int) offset, length).asReadOnlyBuffer());
    }

    @Override
    public NativeCloseableBuffer<IntBuffer> getIndicesRange(long offset, int length, ReadPolicy readPolicy) {
        if (readPolicy.glReadPolicy == GL_READ_WRITE || readPolicy.glReadPolicy == GL_READ_ONLY)
            return new NativeCloseableBuffer<>(indices.slice((int) offset, length));
        return new NativeCloseableBuffer<>(indices.slice((int) offset, length).asReadOnlyBuffer());
    }

    @Override
    public NativeCloseableBuffer<FloatBuffer> getVertices(ReadPolicy readPolicy) {
        if (readPolicy.glReadPolicy == GL_READ_WRITE || readPolicy.glReadPolicy == GL_READ_ONLY)
            return new NativeCloseableBuffer<>(vertices);
        return new NativeCloseableBuffer<>(vertices.asReadOnlyBuffer());
    }

    @Override
    public NativeCloseableBuffer<IntBuffer> getIndices(ReadPolicy readPolicy) {
        if (readPolicy.glReadPolicy == GL_READ_WRITE || readPolicy.glReadPolicy == GL_READ_ONLY)
            return new NativeCloseableBuffer<>(indices);
        return new NativeCloseableBuffer<>(indices.asReadOnlyBuffer());
    }

    @Override
    public void setVerticesRange(long offset, @NonNull FloatBuffer buffer) {
        vertices.put((int) offset, buffer, 0, buffer.limit());
    }

    @Override
    public void setIndicesRange(long offset, @NonNull IntBuffer buffer) {
        indices.put((int) offset, buffer, 0, buffer.limit());
    }

    @Override
    public void setVertices(@NonNull FloatBuffer vertices) {
        BufferUtil.requireNative(vertices);
        if (vertices.limit() == verticesSize) {
            this.vertices.put(0, vertices, 0, (int) verticesSize);
            return;
        }
        MemoryUtil.memFree(this.vertices);
        this.vertices = vertices;
        this.verticesSize = vertices.limit();
    }

    @Override
    public void setIndices(@NonNull IntBuffer indices) {
        BufferUtil.requireNative(indices);
        if (indices.limit() == indicesSize) {
            this.indices.put(0, indices, 0, (int) indicesSize);
            return;
        }
        MemoryUtil.memFree(this.indices);
        this.indices = indices;
        this.indicesSize = indices.limit();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        NativeMesh that = (NativeMesh) o;

        if (verticesSize != that.verticesSize) return false;
        if (indicesSize != that.indicesSize) return false;
        if (!vertices.equals(that.vertices)) return false;
        return indices.equals(that.indices);
    }

    @Override
    public int hashCode() {
        int result = vertices.hashCode();
        result = 31 * result + indices.hashCode();
        result = 31 * result + (int) (verticesSize ^ (verticesSize >>> 32));
        result = 31 * result + (int) (indicesSize ^ (indicesSize >>> 32));
        return result;
    }

    @Override
    public String toString() {
        return STR."""
                \{getClass().getName()}{
                    vertices=\{vertices},
                    indices=\{indices},
                    verticesSize=\{verticesSize},
                    indicesSize=\{indicesSize}
                """;
    }

    @Override
    public void close() {
        MemoryUtil.memFree(indices);
        MemoryUtil.memFree(vertices);
        verticesSize = 0;
        indicesSize = 0;
    }

    public record NativeCloseableBuffer<T extends Buffer>(T buffer) implements CloseableBuffer<T> {
        @Override
        public void close() {}
    }
}

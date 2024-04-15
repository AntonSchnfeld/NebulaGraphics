package com.github.nebula.graphics;

import com.github.nebula.graphics.globjects.Buffer;
import com.github.nebula.graphics.util.BufferUtil;
import lombok.Getter;
import lombok.NonNull;

import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import static org.lwjgl.opengl.GL42C.*;

/**
 * @author Anton Schoenfeld
 * @since 24.03.2024
 */
@Getter
public class GPUMesh implements Mesh {
    private final Buffer vbo, ebo;
    private long verticesSize, indicesSize;

    public GPUMesh() {
        vbo = new Buffer(GL_ARRAY_BUFFER);
        ebo = new Buffer(GL_ELEMENT_ARRAY_BUFFER);
        verticesSize = 0;
        indicesSize = 0;
    }

    @Override
    public GPUCloseableBuffer<FloatBuffer> getVerticesRange(long offset, int length, ReadPolicy readPolicy) {
        return new GPUCloseableBuffer<>(vbo, vbo.mapRange(readPolicy.glAccessPolicy, offset * Float.BYTES, length * Float.BYTES).order(ByteOrder.nativeOrder()).asFloatBuffer());
    }

    @Override
    public GPUCloseableBuffer<IntBuffer> getIndicesRange(long offset, int length, ReadPolicy readPolicy) {
        return new GPUCloseableBuffer<>(ebo, ebo.mapRange(readPolicy.glAccessPolicy, offset * Integer.BYTES, length * Integer.BYTES).order(ByteOrder.nativeOrder()).asIntBuffer());
    }

    @Override
    public GPUCloseableBuffer<FloatBuffer> getVertices(ReadPolicy readPolicy) {
        return new GPUCloseableBuffer<>(vbo, vbo.map(readPolicy.glReadPolicy).asFloatBuffer());
    }

    @Override
    public GPUCloseableBuffer<IntBuffer> getIndices(ReadPolicy readPolicy) {
        return new GPUCloseableBuffer<>(ebo, ebo.map(readPolicy.glReadPolicy).asIntBuffer());
    }

    @Override
    public void setVerticesRange(long offset, @NonNull FloatBuffer buffer) {
        vbo.subData(buffer, offset);
    }

    @Override
    public void setIndicesRange(long offset, @NonNull IntBuffer buffer) {
        ebo.subData(buffer, offset);
    }

    @Override
    public void setVertices(@NonNull FloatBuffer vertices) {
        BufferUtil.validateBufferNativeness(vertices);
        if (verticesSize == vertices.limit()) {
            vbo.subData(vertices, 0);
            return;
        }
        vbo.data(vertices, GL_DYNAMIC_DRAW);
        verticesSize = vertices.limit();
    }

    @Override
    public void setIndices(@NonNull IntBuffer indices) {
        BufferUtil.validateBufferNativeness(indices);
        if (indicesSize == indices.limit()) {
            ebo.subData(indices, 0);
            return;
        }
        ebo.data(indices, GL_DYNAMIC_DRAW);
        indicesSize = indices.limit();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        GPUMesh gpuMesh = (GPUMesh) o;

        if (verticesSize != gpuMesh.verticesSize) return false;
        if (indicesSize != gpuMesh.indicesSize) return false;
        if (!vbo.equals(gpuMesh.vbo)) return false;
        return ebo.equals(gpuMesh.ebo);
    }

    @Override
    public int hashCode() {
        int result = vbo.hashCode();
        result = 31 * result + ebo.hashCode();
        result = 31 * result + (int) (verticesSize ^ (verticesSize >>> 32));
        result = 31 * result + (int) (indicesSize ^ (indicesSize >>> 32));
        return result;
    }

    @Override
    public String toString() {
        return STR."""
                \{getClass().getSimpleName()}{
                    vbo=\{vbo},
                    ebo=\{ebo},
                    verticesSize=\{verticesSize},
                    indicesSize=\{indicesSize}
                }
                """;
    }

    @Override
    public void close() {
        vbo.close();
        ebo.close();
        indicesSize = 0;
        verticesSize = 0;
    }

    public static class GPUCloseableBuffer<T extends java.nio.Buffer> implements CloseableBuffer<T> {
        private final Buffer parent;
        private final T buffer;

        public GPUCloseableBuffer(Buffer parent, T buffer) {
            this.parent = parent;
            this.buffer = buffer;
        }

        public T buffer() {
            return buffer;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            GPUCloseableBuffer<?> that = (GPUCloseableBuffer<?>) o;

            if (!parent.equals(that.parent)) return false;
            return buffer.equals(that.buffer);
        }

        @Override
        public int hashCode() {
            int result = parent.hashCode();
            result = 31 * result + buffer.hashCode();
            return result;
        }

        @Override
        public String toString() {
            return STR."""
                    \{getClass().getName()}{
                        parent=\{parent},
                        buffer=\{buffer}
                    }
                    """;
        }

        @Override
        public void close() {
            parent.unmap();
        }
    }
}
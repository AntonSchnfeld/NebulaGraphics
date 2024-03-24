package com.github.nebula.graphics;

import com.github.nebula.graphics.util.BufferUtil;
import lombok.Getter;
import org.lwjgl.system.MemoryUtil;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;

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
    public FloatBuffer getVerticesRange(long offset, int length, boolean write) {
        if (write) return vertices.slice((int) offset, length);
        return vertices.slice((int) offset, length).asReadOnlyBuffer();
    }

    @Override
    public IntBuffer getIndicesRange(long offset, int length, boolean write) {
        if (write) return indices.slice((int) offset, length);
        return indices.slice((int) offset, length).asReadOnlyBuffer();
    }

    @Override
    public FloatBuffer getVertices(boolean write) {
        return write ? vertices : vertices.asReadOnlyBuffer();
    }

    @Override
    public IntBuffer getIndices(boolean write) {
        return write ? indices : indices.asReadOnlyBuffer();
    }

    @Override
    public void setVerticesRange(long offset, FloatBuffer buffer) {
        vertices.put((int) offset, buffer, 0, buffer.limit());
    }

    @Override
    public void setIndicesRange(long offset, IntBuffer buffer) {
        indices.put((int) offset, buffer, 0, buffer.limit());
    }

    @Override
    public void setVertices(FloatBuffer vertices) {
        BufferUtil.validateBufferNativeness(vertices);
        if (vertices.limit() == verticesSize) {
            this.vertices.put(0, vertices, 0, (int) verticesSize);
            return;
        }
        MemoryUtil.memFree(this.vertices);
        this.vertices = vertices;
        this.verticesSize = vertices.limit();
    }

    @Override
    public void setIndices(IntBuffer indices) {
        BufferUtil.validateBufferNativeness(indices);
        if (indices.limit() == indicesSize) {
            this.indices.put(0, indices, 0, (int) indicesSize);
            return;
        }
        MemoryUtil.memFree(this.indices);
        this.indices = indices;
        this.indicesSize = indices.limit();
    }

    @Override
    public void close() {
        MemoryUtil.memFree(indices);
        MemoryUtil.memFree(vertices);
        verticesSize = 0;
        indicesSize = 0;
    }
}

package com.github.nebula.graphics;

import com.github.nebula.graphics.globjects.Buffer;
import lombok.Getter;

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
    public FloatBuffer getVerticesRange(long offset, int length, boolean write) {
        return vbo.mapRange(write ? GL_READ_WRITE : GL_READ_ONLY, offset, length).asFloatBuffer();
    }

    @Override
    public IntBuffer getIndicesRange(long offset, int length, boolean write) {
        return ebo.mapRange(write ? GL_READ_WRITE : GL_READ_ONLY, offset, length).asIntBuffer();
    }

    @Override
    public FloatBuffer getVertices(boolean write) {
        return vbo.map(write ? GL_READ_WRITE : GL_READ_ONLY).asFloatBuffer();
    }

    @Override
    public IntBuffer getIndices(boolean write) {
        return ebo.map(write ? GL_READ_WRITE : GL_READ_ONLY).asIntBuffer();
    }

    @Override
    public void setVerticesRange(long offset, FloatBuffer buffer) {
        vbo.subData(buffer, offset);
    }

    @Override
    public void setIndicesRange(long offset, IntBuffer buffer) {
        ebo.subData(buffer, offset);
    }

    @Override
    public void setVertices(FloatBuffer vertices) {
        if (verticesSize == vertices.limit()) {
            vbo.subData(vertices, 0);
            return;
        }
        vbo.data(vertices, GL_DYNAMIC_DRAW);
        verticesSize = vertices.limit();
    }

    @Override
    public void setIndices(IntBuffer indices) {
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
}
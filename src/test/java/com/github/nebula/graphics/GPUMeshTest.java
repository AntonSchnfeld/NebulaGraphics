package com.github.nebula.graphics;

import com.github.nebula.graphics.util.BufferUtil;
import com.github.nebula.graphics.window.Window;
import com.github.nebula.graphics.window.WindowHint;
import com.github.nebula.graphics.window.WindowHints;
import lombok.val;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;


class GPUMeshTest implements AutoCloseable {

    private static final float[] VERTICES = new float[]{
            -0.5f, -0.5f,
            0.5f, -0.5f,
            0f, 0.5f
    };

    private static final int[] INDICES = new int[]{
            0, 1, 2
    };

    private Window contextHolder;
    private GPUMesh mesh;

    @BeforeEach
    public void setup() {
        System.setProperty("-debug", Boolean.TRUE.toString());
        val wh = new WindowHints().defaultHints();
        wh.windowHint(WindowHint.VISIBLE, false);
        contextHolder = new Window(wh, getClass().getName());
        contextHolder.createGLCapabilities();

        mesh = new GPUMesh();
        mesh.setVertices(BufferUtil.newNativeFloatBuffer(VERTICES));
        mesh.setIndices(BufferUtil.newNativeIntBuffer(INDICES));
    }

    @Test
    public void getVerticesRange_given_ValidParams_ReadWritePolicy() {
        val startIdx = 1;
        val endIdx = VERTICES.length - 2;
        val len = endIdx - startIdx;

        val newVertexData = new float[len];
        Arrays.fill(newVertexData, 256);

        // Get the vertices spanning from startIdx to endIdx
        //                                                       uses bitmasks in case of vertices range
        try (val vertices = mesh.getVerticesRange(startIdx, len, ReadPolicy.READ_WRITE)) {
            val buf = vertices.buffer();
            // Confirm that the correct vertices are mapped
            buf.position(0);
            for (int i = startIdx, j = 0; j < len; i++, j++) {
                Assertions.assertEquals(VERTICES[i], buf.get(j));
            }

            // Update buffer by writing new values
            buf.put(0, newVertexData, 0, len);
        } // Try with resources automatically unmaps buffer

        // Confirm buffer update was successful
        //                                                       uses bitmasks in case of vertices range
        try (val vertices = mesh.getVerticesRange(startIdx, len, ReadPolicy.READ)) {
            val buf = vertices.buffer();

            for (int i = 0; i < len; i++) {
                Assertions.assertEquals(newVertexData[i], buf.get());
            }
        } // Try with resources automatically unmaps buffer
    }

    @Override
    @AfterEach
    public void close() {
        mesh.close();
        contextHolder.close();
    }
}
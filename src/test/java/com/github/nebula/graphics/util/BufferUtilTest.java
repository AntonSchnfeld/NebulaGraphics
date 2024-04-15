package com.github.nebula.graphics.util;

import com.github.nebula.graphics.GPUMesh;
import com.github.nebula.graphics.NativeMesh;
import com.github.nebula.graphics.ReadPolicy;
import com.github.nebula.graphics.window.Window;
import com.github.nebula.graphics.window.WindowHint;
import com.github.nebula.graphics.window.WindowHints;
import lombok.val;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.lwjgl.system.MemoryUtil;

import java.nio.Buffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;

class BufferUtilTest {

    @BeforeAll
    public static void setup() {
        System.setProperty("-debug", Boolean.TRUE.toString());
    }

    @Test
    public void requireNative_given_indirectBuffer() {
        val indirectBuffer = IntBuffer.allocate(10);
        Assertions.assertThrows(IllegalArgumentException.class,
                () -> BufferUtil.requireNative(indirectBuffer));
    }

    @Test
    public void requireNative_given_directBuffer() {
        val directBuffer = MemoryUtil.memAllocInt(10);
        Assertions.assertDoesNotThrow(() ->
                BufferUtil.requireNative(directBuffer));
        MemoryUtil.memFree(directBuffer);
    }

    @Test
    public void getCombinedBufferSize() {
        val buffers = new Buffer[]{
                IntBuffer.allocate(10),
                IntBuffer.allocate(10),
                IntBuffer.allocate(10),
                IntBuffer.allocate(5)
        };
        val combinedBufferSize = BufferUtil.getCombinedBufferSize(buffers);
        Assertions.assertEquals(combinedBufferSize, 35);
    }

    @Test
    public void concatFloatBuffers() {
        val floatBuffer1 = FloatBuffer.wrap(new float[]{
                0, 1, 2, 3, 4, 5
        });
        val floatBuffer2 = FloatBuffer.wrap(new float[]{
                6, 7, 8, 9, 10
        });

        val concatFloatBuffer = BufferUtil.concatFloatBuffers(floatBuffer1, floatBuffer2);

        val expectedValue = FloatBuffer.wrap(new float[]{
                0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10
        });

        Assertions.assertEquals(concatFloatBuffer.limit(), expectedValue.limit());

        for (int i = 0; i < concatFloatBuffer.limit(); i++)
            Assertions.assertEquals(concatFloatBuffer.get(i), expectedValue.get(i));

        MemoryUtil.memFree(concatFloatBuffer);
    }

    @Test
    public void concatIndexBuffers() {
        val quadIndexBuffer = IntBuffer.wrap(new int[]{
                0, 1, 2,
                0, 2, 3
        });

        val concatIndexBuffer = BufferUtil.concatIndexBuffers(quadIndexBuffer, quadIndexBuffer);

        val expectedValue = IntBuffer.wrap(new int[]{
                0, 1, 2,
                0, 2, 3,

                4, 5, 6,
                4, 6, 7
        });

        Assertions.assertEquals(expectedValue.limit(), concatIndexBuffer.limit());

        for (int i = 0; i < concatIndexBuffer.limit(); i++)
            Assertions.assertEquals(concatIndexBuffer.get(i), expectedValue.get(i));

        MemoryUtil.memFree(concatIndexBuffer);
    }

    @Test
    public void concatMeshes_given_GPUMeshes() {
        float[] vertices = {1, 2, 3,};
        int[] indices = {1, 2, 3,};
        float[] expectedVertices = {1, 2, 3, 1, 2, 3,};
        int[] expectedIndices = {1, 2, 3, 5, 6, 7};

        WindowHints wh = new WindowHints().defaultHints().windowHint(WindowHint.VISIBLE, false);
        try (Window context = new Window(wh, getClass().getName())) {
            context.createGLCapabilities();
            try (GPUMesh mesh = new GPUMesh(); GPUMesh mesh2 = new GPUMesh()) {
                FloatBuffer floatBuffer = BufferUtil.newNativeFloatBuffer(vertices);
                IntBuffer intBuffer = BufferUtil.newNativeIntBuffer(indices);
                mesh.setVertices(floatBuffer);
                mesh.setIndices(intBuffer);
                mesh2.setVertices(floatBuffer);
                mesh2.setIndices(intBuffer);
                MemoryUtil.memFree(floatBuffer);
                MemoryUtil.memFree(intBuffer);

                NativeMesh result = BufferUtil.concatMeshes(mesh, mesh2);

                Assertions.assertEquals(expectedVertices.length, result.getVerticesSize());
                Assertions.assertEquals(expectedIndices.length, result.getIndicesSize());

                try (var resultMeshVertices = result.getVertices(ReadPolicy.READ)) {
                    FloatBuffer buf = resultMeshVertices.buffer();
                    Assertions.assertEquals(expectedVertices.length, buf.limit());
                    for (int i = 0; i < expectedVertices.length; i++)
                        Assertions.assertEquals(expectedVertices[i], buf.get(i));
                }
                try (var resultMeshIndices = result.getIndices(ReadPolicy.READ)) {
                    IntBuffer buf = resultMeshIndices.buffer();
                    buf.flip();
                    System.out.flush();
                    Assertions.assertEquals(expectedIndices.length, buf.limit());
                    for (int i = 0; i < expectedIndices.length; i++)
                        Assertions.assertEquals(expectedIndices[i], buf.get(i));
                }
            }
        }
    }
}
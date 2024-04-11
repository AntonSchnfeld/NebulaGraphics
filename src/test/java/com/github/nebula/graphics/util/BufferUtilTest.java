package com.github.nebula.graphics.util;

import com.github.nebula.graphics.GPUMesh;
import com.github.nebula.graphics.NativeMesh;
import com.github.nebula.graphics.window.Window;
import com.github.nebula.graphics.window.WindowHint;
import com.github.nebula.graphics.window.WindowHints;
import lombok.val;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.lwjgl.system.MemoryUtil;

import java.nio.Buffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;

class BufferUtilTest {

    static {
        System.setProperty("-debug", "true");
    }

    @Test
    public void validateBufferNativeness_given_indirectBuffer() {
        val indirectBuffer = IntBuffer.allocate(10);
        Assertions.assertThrows(IllegalArgumentException.class,
                () -> BufferUtil.validateBufferNativeness(indirectBuffer));
    }

    @Test
    public void validateBufferNativeness_given_directBuffer() {
        val directBuffer = MemoryUtil.memAllocInt(10);
        Assertions.assertDoesNotThrow(() ->
                BufferUtil.validateBufferNativeness(directBuffer));
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
        val hints = new WindowHints().defaultHints();
        hints.windowHint(WindowHint.VISIBLE, false);
        try (val window = new Window(hints, getClass().getName())) {
            window.createGLCapabilities();
            try (val mesh = new GPUMesh();
                 val expectedValue = new NativeMesh()) {
                mesh.setVertices(MemoryUtil.memAllocFloat(6).put(new float[]{
                        0, 1, 2, 3, 4, 5
                }));
                expectedValue.setVertices(MemoryUtil.memAllocFloat(12).put(new float[]{
                        0, 1, 2, 3, 4, 5, 0, 1, 2, 3, 4, 5
                }));
                val result = BufferUtil.concatMeshes(mesh, mesh);

                try (val closeableResultVertices = result.getVertices(false);
                     val closeableExpectedVertices = expectedValue.getVertices(false)) {
                    val resultVertices = closeableResultVertices.buffer();
                    val expectedVertices = closeableExpectedVertices.buffer();
                    resultVertices.position(0);
                    expectedVertices.position(0);

                    Assertions.assertEquals(resultVertices.limit(), expectedVertices.limit());
                    for (int i = 0; i < resultVertices.limit(); i++) {
                        Assertions.assertEquals(resultVertices.get(i), expectedVertices.get(i));
                    }
                }
            }
        }
    }
}
package com.github.nebula.graphics.util;

import com.github.nebula.graphics.Mesh;
import com.github.nebula.graphics.NativeMesh;
import lombok.val;
import org.lwjgl.system.MemoryUtil;

import java.nio.Buffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;

/**
 * @author Anton Schoenfeld
 * @since 24.03.2024
 */
public final class BufferUtil {
    public static void validateBufferNativeness(Buffer buffer) {
        if (!buffer.isDirect()) {
            throw new IllegalArgumentException("Expected direct buffer, received heap buffer");
        }
    }

    public static NativeMesh concatMeshes(Mesh... meshes) {
        return concatMeshes(new NativeMesh(), meshes);
    }

    public static IntBuffer getDefaultIndexBuffer(int numVertices) {
        val ebo = MemoryUtil.memAllocInt(numVertices);
        for (int i = 0; i < numVertices; i++) ebo.put(i);
        return ebo;
    }

    public static <T extends Mesh> T concatMeshes(T concatMesh, Mesh... meshes) {
        val len = meshes.length;
        val floatBuffers = new FloatBuffer[len];
        val indexBuffers = new IntBuffer[len];

        for (var i = 0; i < len; i++) {
            floatBuffers[i] = meshes[i].getVertices(false);
            indexBuffers[i] = meshes[i].getIndices(false);
        }

        concatMesh.setVertices(concatFloatBuffers(floatBuffers));
        concatMesh.setIndices(concatIndexBuffers(indexBuffers));

        return concatMesh;
    }

    public static int getCombinedBufferSize(Buffer... buffers) {
        var size = 0;
        for (val buffer : buffers)
            size += buffer.limit();
        return size;
    }

    public static FloatBuffer concatFloatBuffers(FloatBuffer... buffers) {
        val combinedSize = getCombinedBufferSize(buffers);
        val concatBuffer = MemoryUtil.memAllocFloat(combinedSize);

        var index = 0;
        for (val buffer : buffers) {
            val bufferLimit = buffer.limit();
            concatBuffer.put(index, buffer, 0, bufferLimit);
            index += bufferLimit;
        }

        return concatBuffer;
    }

    public static IntBuffer concatIndexBuffers(IntBuffer... indexBuffers) {
        val combinedSize = getCombinedBufferSize(indexBuffers);
        val concatBuffer = MemoryUtil.memAllocInt(combinedSize);

        var highestIndex = 0;
        for (val indexBuffer : indexBuffers) {
            var currentHighestIndex = 0;

            indexBuffer.position(0);
            while (indexBuffer.hasRemaining()) {
                val n = indexBuffer.get();
                if (n > currentHighestIndex) currentHighestIndex = n;
                concatBuffer.put(n + highestIndex);
            }

            highestIndex += currentHighestIndex + 1;
        }

        return concatBuffer;
    }
}

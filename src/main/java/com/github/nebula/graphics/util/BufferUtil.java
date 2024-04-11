package com.github.nebula.graphics.util;

import com.github.nebula.graphics.Mesh;
import com.github.nebula.graphics.NativeMesh;
import io.reactivex.rxjava3.annotations.NonNull;
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

    @NonNull
    public static NativeMesh concatMeshes(@NonNull Mesh @NonNull ... meshes) {
        return concatMeshesIntoMesh(new NativeMesh(), meshes);
    }

    @NonNull
    public static IntBuffer getDefaultIndexBuffer(int numVertices) {
        val ebo = MemoryUtil.memAllocInt(numVertices);
        for (int i = 0; i < numVertices; i++) ebo.put(i);
        return ebo;
    }

    @NonNull
    public static <T extends Mesh> T concatMeshesIntoMesh(@NonNull T concatMesh, @NonNull Mesh @NonNull ... meshes) {
        val len = meshes.length;
        val floatBuffers = new FloatBuffer[len];
        val indexBuffers = new IntBuffer[len];

        for (var i = 0; i < len; i++) {
            try (val floatBuffer = meshes[i].getVertices(false);
                 val indexBuffer = meshes[i].getIndices(false)) {
                floatBuffers[i] = floatBuffer.buffer();
                indexBuffers[i] = indexBuffer.buffer();
            }
        }

        concatMesh.setVertices(concatFloatBuffers(floatBuffers));
        concatMesh.setIndices(concatIndexBuffers(indexBuffers));

        return concatMesh;
    }

    public static int getCombinedBufferSize(@NonNull Buffer @NonNull ... buffers) {
        var size = 0;
        for (val buffer : buffers)
            size += buffer.limit();
        return size;
    }

    @NonNull
    public static FloatBuffer concatFloatBuffers(@NonNull FloatBuffer @NonNull ... buffers) {
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

    @NonNull
    public static IntBuffer concatIndexBuffers(@NonNull IntBuffer @NonNull ... indexBuffers) {
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

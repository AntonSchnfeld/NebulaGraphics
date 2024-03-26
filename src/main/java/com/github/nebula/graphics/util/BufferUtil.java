package com.github.nebula.graphics.util;

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

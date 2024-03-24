package com.github.nebula.graphics.util;

import java.nio.Buffer;

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
}

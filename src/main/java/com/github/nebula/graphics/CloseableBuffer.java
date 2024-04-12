package com.github.nebula.graphics;

import java.nio.Buffer;

/**
 * @author Anton Schoenfeld
 * @since 12.04.2024
 * @param <T> Type of the stored buffer
 */
public interface CloseableBuffer<T extends Buffer> extends AutoCloseable {
    T buffer();

    void close();
}

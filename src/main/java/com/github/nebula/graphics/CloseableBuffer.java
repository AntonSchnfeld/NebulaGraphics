package com.github.nebula.graphics;

import java.nio.Buffer;

/**
 * @param <T> Type of the stored buffer
 * @author Anton Schoenfeld
 * @since 12.04.2024
 */
public interface CloseableBuffer<T extends Buffer> extends AutoCloseable {
    T buffer();

    void close();
}

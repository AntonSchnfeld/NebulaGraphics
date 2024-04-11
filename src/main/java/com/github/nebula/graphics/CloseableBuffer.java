package com.github.nebula.graphics;

import java.nio.Buffer;

public interface CloseableBuffer<T extends Buffer> extends AutoCloseable {
    T buffer();

    void close();
}

package org.nebula.graphics.globjects;

public abstract class OpenGLObject implements AutoCloseable {
    public final int id;

    public OpenGLObject(final int id) {
        this.id = id;
    }
}

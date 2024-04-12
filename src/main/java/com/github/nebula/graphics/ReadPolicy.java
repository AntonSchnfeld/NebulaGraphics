package com.github.nebula.graphics;

import lombok.AllArgsConstructor;
import static org.lwjgl.opengl.GL43C.*;

@AllArgsConstructor
public enum ReadPolicy {
    READ(GL_READ_ONLY, GL_MAP_READ_BIT),
    WRITE(GL_WRITE_ONLY, GL_MAP_WRITE_BIT),
    READ_WRITE(GL_READ_WRITE, GL_MAP_WRITE_BIT | GL_MAP_READ_BIT),;

    public final int glReadPolicy;
    public final int glAccessPolicy;
}

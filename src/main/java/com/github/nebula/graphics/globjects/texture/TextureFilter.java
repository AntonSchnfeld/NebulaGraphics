package com.github.nebula.graphics.globjects.texture;

import static org.lwjgl.opengl.GL33C.GL_LINEAR;

/**
 * The {@code TextureFilter} class represents the filter parameters used in texture sampling.
 * It encapsulates the magnification and minification filter types.
 *
 * <p>Instances of this class are immutable and provide a convenient way to specify texture filter
 * configurations for texture objects in OpenGL-based applications.
 *
 * <p>The class provides a default constructor that initializes the filter parameters with
 * commonly used values for magnification and minification filters, both set to GL_LINEAR.
 *
 * @param magFilter The magnification filter type.
 * @param minFilter The minification filter type.
 * @author Anton Schoenfeld
 * @since 12.03.2024
 */
public record TextureFilter(int magFilter, int minFilter) {

    /**
     * Constructs a TextureFilter with default filter values.
     * The default configuration sets both the magnification and minification filters to GL_LINEAR.
     */
    public TextureFilter() {
        this(GL_LINEAR, GL_LINEAR);
    }
}

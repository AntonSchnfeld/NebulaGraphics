package com.github.nebula.graphics.globjects.texture;

import static org.lwjgl.opengl.GL33C.GL_RGBA;
import static org.lwjgl.opengl.GL33C.GL_UNSIGNED_BYTE;

/**
 * The {@code TextureConfig} class represents the configuration parameters for creating a texture.
 * It is designed to encapsulate the parameters necessary for texture creation, including
 * the mip map level, texel data type, and texture format.
 *
 * <p>Instances of this class are immutable and can be used to specify the configuration
 * for creating textures in OpenGL-based applications.
 *
 * <p>The class provides a default constructor that initializes the configuration with
 * commonly used values for mip map level, texel data type, and texture format.
 *
 * @param mipMapLevel   The mip map level of the texture.
 * @param texelDataType The data type of texels in the texture.
 * @param format        The format of the texture.
 * @author Anton Schoenfeld
 * @since 12.03.2024
 */
public record TextureConfig(int mipMapLevel, int texelDataType, int format) {

    /**
     * Constructs a TextureConfig with default configuration values.
     * The default configuration sets the mip map level to 0, texel data type to GL_UNSIGNED_BYTE,
     * and texture format to GL_RGBA.
     */
    public TextureConfig() {
        this(0, GL_UNSIGNED_BYTE, GL_RGBA);
    }
}

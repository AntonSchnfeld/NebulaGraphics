package com.github.nebula.graphics.globjects;

import com.github.nebula.graphics.data.ByteBufferedImage;
import com.github.nebula.graphics.globjects.texture.TextureConfig;
import com.github.nebula.graphics.globjects.texture.TextureDimensions;
import com.github.nebula.graphics.globjects.texture.TextureFilter;
import lombok.Getter;

import static org.lwjgl.opengl.GL33C.*;
import static org.lwjgl.system.MemoryUtil.NULL;

/**
 * The {@code Texture} class represents an OpenGL texture, providing functionalities for texture creation,
 * loading from images, and resource management. It encapsulates the OpenGL texture ID, dimensions, and color channels.
 *
 * <p>Instances of this class can be created either with a specified width and height for a blank texture or by loading
 * an image from a given URL. The texture parameters, such as filtering and wrapping, can be customized during creation.
 *
 * <p>When the texture is no longer needed, it should be closed using the {@link #close()} method to release OpenGL resources.
 *
 * <p>Additionally, the class provides methods for binding the texture to the OpenGL context, unbinding, and binding to
 * a specific texture unit slot.
 *
 * @author Anton Schoenfeld
 * @since 07.03.2024
 */
@Getter
public final class Texture extends OpenGLObject implements FrameBufferAttachment {
    /**
     * The dimensions (width and height) of the texture.
     */
    private final TextureDimensions dimensions;

    /**
     * The filter parameters for texture sampling.
     */
    private final TextureFilter filter;

    /**
     * The configuration parameters for texture creation.
     */
    private final TextureConfig config;

    /**
     * Constructs a Texture with the specified dimensions, filter, and configuration.
     *
     * @param dimensions The dimensions (width and height) of the texture.
     * @param filter     The filter parameters for texture sampling.
     * @param config     The configuration parameters for texture creation.
     */
    public Texture(TextureDimensions dimensions, TextureFilter filter, TextureConfig config) {
        super(glGenTextures());
        this.dimensions = dimensions;
        this.config = config;
        this.filter = filter;

        bind();
        glTexImage2D(
                GL_TEXTURE_2D, config.mipMapLevel(), config.format(), dimensions.width(), dimensions.height(), 0,
                config.format(), config.texelDataType(), NULL
        );

        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, filter.minFilter());
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, filter.magFilter());
        unbind();
    }

    /**
     * Constructs a Texture with the specified dimensions and default filter and configuration.
     *
     * @param dimensions The dimensions (width and height) of the texture.
     */
    public Texture(TextureDimensions dimensions) {
        this(dimensions, new TextureFilter(), new TextureConfig());
    }

    /**
     * Constructs a Texture from the provided image with the specified filter and configuration.
     *
     * @param image  The image data used to create the texture.
     * @param filter The filter parameters for texture sampling.
     * @param config The configuration parameters for texture creation.
     */
    public Texture(final ByteBufferedImage image, TextureFilter filter, TextureConfig config) {
        super(glGenTextures());
        glBindTexture(GL_TEXTURE_2D, id);

        this.filter = filter;
        this.config = config;
        this.dimensions = new TextureDimensions(image.width(), image.height());

        // Set texture parameters
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_REPEAT); // Repeat texture when stretched
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_REPEAT);

        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, filter.magFilter());
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, filter.minFilter());

        glTexImage2D(GL_TEXTURE_2D, config.mipMapLevel(), config.format(), image.width(), image.height(),
                0, config.format(), GL_UNSIGNED_BYTE, image.bytes());
    }

    /**
     * Constructs a Texture from the provided image with default filter and configuration.
     *
     * @param image The image data used to create the texture.
     */
    public Texture(ByteBufferedImage image) {
        this(image, new TextureFilter(), new TextureConfig());
    }

    /**
     * Binds the texture to the OpenGL context.
     */
    public void bind() {
        glBindTexture(GL_TEXTURE_2D, id);
    }

    /**
     * Binds the texture to a specific texture unit slot.
     *
     * @param slot The texture unit slot to bind the texture to.
     */
    public void bindToSlot(int slot) {
        glActiveTexture(GL_TEXTURE0 + slot);
        glBindTexture(GL_TEXTURE_2D, id);
    }

    /**
     * Releases OpenGL resources associated with the texture.
     */
    @Override
    public void close() {
        glDeleteTextures(id);
    }

    /**
     * Unbinds the texture from the OpenGL context.
     */
    public void unbind() {
        glBindTexture(GL_TEXTURE_2D, 0);
    }
}
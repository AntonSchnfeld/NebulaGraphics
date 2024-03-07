package org.nebula.graphics.globjects;

import lombok.Getter;
import org.nebula.graphics.data.ByteBufferedImage;

import java.io.IOException;
import java.net.URL;

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
public class Texture implements AutoCloseable {
    private final int id, width, height, channels;

    /**
     * Constructs a blank texture with the specified width and height.
     *
     * @param width  The width of the texture.
     * @param height The height of the texture.
     */
    public Texture(int width, int height) {
        this.id = glGenTextures();
        this.width = width;
        this.height = height;
        this.channels = 3;

        bind();
        glTexImage2D(
                GL_TEXTURE_2D, 0, GL_RGB, width, height, 0,
                GL_RGB, GL_UNSIGNED_BYTE, NULL
        );
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
        unbind();
    }

    /**
     * Constructs a texture by loading an image from the specified URL.
     *
     * @param url The URL of the image to load.
     * @throws IOException If an I/O error occurs during image loading.
     */
    public Texture(URL url) throws IOException {
        this(url, false);
    }

    /**
     * Constructs a texture by loading an image from the specified URL, with an option for anti-aliasing.
     *
     * @param url             The URL of the image to load.
     * @param useAntiAliasing Flag indicating whether to use anti-aliasing.
     * @throws IOException If an I/O error occurs during image loading.
     */
    public Texture(URL url, boolean useAntiAliasing) throws IOException {
        this(ByteBufferedImage.fromURL(url), useAntiAliasing);
    }

    /**
     * Constructs a texture from a ByteBufferedImage.
     *
     * @param image The ByteBufferedImage containing the image data.
     */
    public Texture(final ByteBufferedImage image) {
        this(image, false);
    }

    /**
     * Constructs a texture from a ByteBufferedImage, with an option for anti-aliasing.
     *
     * @param image           The ByteBufferedImage containing the image data.
     * @param useAntiAliasing Flag indicating whether to use anti-aliasing.
     */
    public Texture(final ByteBufferedImage image, boolean useAntiAliasing) {
        id = glGenTextures();
        glBindTexture(GL_TEXTURE_2D, id);

        // Set texture parameters
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_REPEAT); // Repeat texture when stretched
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_REPEAT);
        final int magFilter = useAntiAliasing ? GL_LINEAR : GL_NEAREST;
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, magFilter);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, magFilter);

        width = image.width();
        height = image.height();
        channels = image.channels();

        final int colorMode = image.channels() == 4 ? GL_RGBA : GL_RGB;
        glTexImage2D(GL_TEXTURE_2D, 0, colorMode, image.width(), image.height(),
                0, colorMode, GL_UNSIGNED_BYTE, image.bytes());
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
     * @param slot The texture unit slot.
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
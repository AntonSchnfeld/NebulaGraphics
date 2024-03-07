package org.nebula.graphics.data;

import lombok.NonNull;
import org.lwjgl.stb.STBImage;
import org.lwjgl.system.MemoryStack;
import org.lwjgl.system.MemoryUtil;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.ByteBuffer;

import static org.lwjgl.stb.STBImage.STBI_default;
import static org.lwjgl.stb.STBImage.stbi_image_free;

/**
 * The {@code ByteBufferedImage} class represents an image loaded into memory as a {@link java.nio.ByteBuffer}.
 * It is designed to be used with the Lightweight Java Game Library (LWJGL) and STB Image for image loading.
 * Instances of this class encapsulate image data, including pixel values, dimensions, and color channels.
 *
 * <p>This class implements the {@link java.lang.AutoCloseable} interface, allowing for convenient resource cleanup.
 * When an instance is no longer needed, calling the {@link #close()} method releases the allocated native memory.
 *
 * <p>The primary use case for this class is to load images from various sources, such as URLs, and provide
 * easy access to the image data for further processing within LWJGL-based applications.
 *
 * @author Anton Schoenfeld
 * @since 07.03.2024
 */
public record ByteBufferedImage(ByteBuffer bytes, int width, int height, int channels) implements AutoCloseable {

    /**
     * Creates a ByteBufferedImage by loading an image from the specified URL.
     *
     * @param url The URL of the image to load.
     * @return A ByteBufferedImage containing the image data.
     * @throws IOException If an I/O error occurs during image loading.
     */
    public static ByteBufferedImage fromURL(@NonNull URL url) throws IOException {
        ByteBufferedImage bbi;

        try (InputStream is = url.openStream(); MemoryStack stack = MemoryStack.stackPush()) {
            // Read all bytes from the input stream
            byte[] bytes = is.readAllBytes();

            // Allocate native memory and copy image bytes into the ByteBuffer
            var buf = MemoryUtil.memAlloc(bytes.length);
            buf.put(bytes, 0, bytes.length);
            buf.flip();

            // Allocate memory for image metadata (width, height, channels)
            var width = stack.mallocInt(1);
            var height = stack.mallocInt(1);
            var channels = stack.mallocInt(1);

            // Load image from memory using STB Image
            ByteBuffer imageBuf = STBImage.stbi_load_from_memory(buf, width, height, channels, STBI_default);
            if (imageBuf == null)
                throw new IOException("STB Image failed to load file from memory. Failure reason: "
                        + STBImage.stbi_failure_reason());

            // Create a ByteBufferedImage instance with the loaded image data and metadata
            bbi = new ByteBufferedImage(imageBuf, width.get(), height.get(), channels.get());
        }

        return bbi;
    }

    /**
     * Closes the resources associated with the ByteBufferedImage.
     * Releases the native memory allocated for the image.
     */
    @Override
    public void close() {
        stbi_image_free(bytes);
    }
}

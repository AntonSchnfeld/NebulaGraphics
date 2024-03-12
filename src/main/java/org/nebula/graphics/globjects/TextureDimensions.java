package org.nebula.graphics.globjects;

/**
 * The {@code TextureDimensions} class represents the dimensions (width and height) of a texture.
 * It ensures that the provided dimensions are valid, with both width and height greater than 0.
 *
 * <p>Instances of this class are immutable and provide a convenient way to specify texture dimensions
 * in OpenGL-based applications.
 *
 * <p>The class checks the validity of the provided dimensions in its constructor, throwing an
 * IllegalArgumentException if either the width or height is less than 1.
 *
 * @param width  The width of the texture.
 * @param height The height of the texture.
 * @author Anton Schoenfeld
 * @since 12.03.2024
 */
public record TextureDimensions(int width, int height) {

    /**
     * Constructs a TextureDimensions object with the provided width and height.
     * It validates that both the width and height are greater than 0.
     *
     * @throws IllegalArgumentException if either the width or height is less than 1.
     */
    public TextureDimensions {
        if (width < 1 || height < 1)
            throw new IllegalArgumentException("Width and height must be larger than 0");
    }
}
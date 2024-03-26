package com.github.nebula.graphics;

import com.github.nebula.graphics.data.UniformAttributes;
import com.github.nebula.graphics.globjects.Shader;

/**
 * The {@code Material} interface represents a material used for rendering objects in a graphical scene.
 * Materials define properties such as colors, textures, and shaders that determine the appearance of rendered geometry.
 * Implementations of this interface provide methods for applying the material to shaders,
 * setting and getting uniform attributes, and checking compatibility with shaders and uniform attributes.
 * <p>
 * Materials can be bound to shaders either directly or by checking compatibility first.
 * When applying a material to a shader, implementations may throw {@link IllegalArgumentException}
 * if the shader is not compatible with the material.
 * <p>
 * Implementations of this interface are expected to properly manage resources
 * and may extend {@link AutoCloseable} to facilitate resource cleanup.
 *
 * @author Anton Schoenfeld
 * @since 26.03.2024
 */
public interface Material extends AutoCloseable {
    /**
     * Applies the material to the currently bound shader.
     */
    void bind();

    /**
     * Applies the material to the specified shader. This will bind the shader.
     * Implementations may throw {@link IllegalArgumentException} if the shader is not compatible with this material.
     *
     * @param shader the shader to which the material will be applied
     */
    void bind(Shader shader);

    /**
     * Checks whether this material is compatible with the specified shader.
     *
     * @param shader the shader to check for compatibility
     * @return {@code true} if this material is compatible with the shader, {@code false} otherwise
     */
    boolean isCompatible(Shader shader);

    /**
     * Checks whether this material is compatible with the specified uniform attributes.
     *
     * @param uniformAttributes the uniform attributes to check for compatibility
     * @return {@code true} if this material is compatible with the uniform attributes, {@code false} otherwise
     */
    boolean isCompatible(UniformAttributes uniformAttributes);

    /**
     * Sets a uniform attribute to the specified value.
     * Implementations may throw {@link IllegalArgumentException} if the class of the value
     * is not an instance of the class expected by the attribute.
     *
     * @param name  the name of the uniform attribute
     * @param value the new value of the uniform attribute
     */
    void setUniform(String name, Object value);

    /**
     * Gets the value of a uniform attribute.
     *
     * @param name the name of the uniform attribute
     * @return the value of the uniform attribute
     */
    Object getUniform(String name);
}
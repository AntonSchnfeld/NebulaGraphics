package com.github.nebula.graphics.data;

/**
 * Represents a single vertex attribute used in a GLSL shader.
 * Each vertex attribute consists of a data type, a name, and a location.
 *
 * @author Anton Schoenfeld
 * @since 21.03.2024
 */
public record VertexAttribute(GLSLDataType dataType, String name, int location) {
    /**
     * Constructs a new VertexAttribute with the specified data type, name, and location.
     *
     * @param dataType The data type of the vertex attribute.
     * @param name     The name of the vertex attribute.
     * @param location The location index of the vertex attribute.
     */
    public VertexAttribute {}
}
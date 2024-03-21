package com.github.nebula.graphics.data;

import io.reactivex.rxjava3.annotations.NonNull;
import lombok.val;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.nebula.util.Poolable;

/**
 * The {@code Vertex} class represents a vertex in three-dimensional space and contains information
 * on the position, normal, and texture coordinates. This class is designed to be used in graphics
 * applications and is a record, immutable by default.
 *
 * <p>It implements the {@link Poolable} interface, allowing instances to be efficiently managed by a
 * {@link org.nebula.util.Pool}.
 *
 * @author Anton Schoenfeld
 * @since 07.03.2024
 */
public record Vertex(Vector3f position, Vector3f normal, Vector2f textureCoord) implements Poolable {

    /**
     * Constructs a new {@code Vertex} with the specified coordinates for position, normal, and texture coordinates.
     *
     * @param x  X-coordinate of the position.
     * @param y  Y-coordinate of the position.
     * @param z  Z-coordinate of the position.
     * @param nx X-coordinate of the normal.
     * @param ny Y-coordinate of the normal.
     * @param nz Z-coordinate of the normal.
     * @param u  U-coordinate of the texture.
     * @param v  V-coordinate of the texture.
     */
    public Vertex(float x, float y, float z, float nx, float ny, float nz, float u, float v) {
        this(new Vector3f(x, y, z), new Vector3f(nx, ny, nz), new Vector2f(u, v));
    }

    /**
     * Constructs a new {@code Vertex} by copying another vertex.
     *
     * @param vertex The vertex to copy.
     */
    public Vertex(@NonNull Vertex vertex) {
        this(new Vector3f(vertex.position), new Vector3f(vertex.normal), new Vector2f(vertex.textureCoord));
    }

    /**
     * Constructs a new {@code Vertex} with default values for position, normal, and texture coordinates.
     */
    public Vertex() {
        this(new Vector3f(), new Vector3f(), new Vector2f());
    }

    /**
     * Resets the state of the vertex, setting all coordinates to zero.
     */
    @Override
    public void reset() {
        position.set(0);
        normal.set(0);
        textureCoord.set(0);
    }

    /**
     * Creates a new {@code Vertex} with a specified position while copying other attributes.
     *
     * @param position The new position for the vertex.
     * @return A new {@code Vertex} with the specified position.
     */
    @NonNull
    public Vertex withPosition(@NonNull Vector3f position) {
        val withPosition = new Vertex(this);
        withPosition.position.set(position);
        return withPosition;
    }

    /**
     * Creates a new {@code Vertex} with a specified normal while copying other attributes.
     *
     * @param normal The new normal for the vertex.
     * @return A new {@code Vertex} with the specified normal.
     */
    @NonNull
    public Vertex withNormal(@NonNull Vector3f normal) {
        val withNormal = new Vertex(this);
        withNormal.normal.set(normal);
        return withNormal;
    }

    /**
     * Creates a new {@code Vertex} with specified texture coordinates while copying other attributes.
     *
     * @param textureCoord The new texture coordinates for the vertex.
     * @return A new {@code Vertex} with the specified texture coordinates.
     */
    @NonNull
    public Vertex withTextureCoord(@NonNull Vector2f textureCoord) {
        val withTextureCoord = new Vertex(this);
        withTextureCoord.textureCoord.set(textureCoord);
        return withTextureCoord;
    }

    /**
     * Returns a string representation of the vertex, including its position, normal, and texture coordinates.
     *
     * @return A string representation of the vertex.
     */
    @Override
    @NonNull
    public String toString() {
        return "Vertex{" +
                "position=" + position +
                ", normal=" + normal +
                ", textureCoord=" + textureCoord +
                '}';
    }
}
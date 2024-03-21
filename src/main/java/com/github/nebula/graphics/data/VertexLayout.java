package com.github.nebula.graphics.data;

import io.reactivex.rxjava3.annotations.NonNull;
import lombok.val;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;

/**
 * Represents the layout of vertex attributes used in a GLSL shader.
 * A vertex layout defines the arrangement and types of attributes associated with each vertex.
 *
 * @author Anton Schoenfeld
 * @since 21.03.2024
 */
public final class VertexLayout implements Iterable<VertexAttribute> {
    private final VertexAttribute[] layout;

    /**
     * Constructs a new VertexLayout with the specified vertex attribute layout.
     * Makes a defensive copy of the layout array to prevent external modification.
     *
     * @param layout The vertex attribute layout to be used.
     * @throws InvalidVertexLayoutException If the provided layout is invalid.
     *                                      An invalid layout occurs when the attributes are not in sequential order
     *                                      starting from location 0 or when two attributes have the same name.
     */
    public VertexLayout(@NonNull VertexAttribute... layout) {
        validateLayout(layout);
        this.layout = Arrays.copyOf(layout, layout.length);
    }

    /**
     * Validates the vertex attribute layout to ensure attributes are in sequential order starting from location 0
     * and that no duplicate attribute names are present.
     * Throws an exception if the layout is invalid.
     *
     * @param attributes The vertex attributes to validate.
     * @throws InvalidVertexLayoutException If the layout is invalid.
     */
    private void validateLayout(VertexAttribute[] attributes) {
        val len = attributes.length;
        val names = new HashSet<String>(attributes.length);

        for (var i = 0; i < len; i++) {
            val cur = attributes[i];
            if (cur.location() != i)
                throw new InvalidVertexLayoutException("Expected location " + i + " but found location " + cur.location());
            if (!names.add(cur.name()))
                throw new InvalidVertexLayoutException("Duplicate name: " + cur.name());
        }
    }

    /**
     * Retrieves the vertex attribute at the specified index.
     *
     * @param index The index of the vertex attribute to retrieve.
     * @return The vertex attribute at the specified index.
     */
    @NonNull
    public VertexAttribute get(int index) {
        return layout[index];
    }

    /**
     * Returns an iterator over the vertex attributes in this layout.
     * The iterator traverses the attributes in sequential order.
     *
     * @return An iterator over the vertex attributes.
     */
    @Override
    @NonNull
    public Iterator<VertexAttribute> iterator() {
        return new Iterator<>() {
            private int index = 0;

            @Override
            public boolean hasNext() {
                return index < layout.length;
            }

            @Override
            public VertexAttribute next() {
                return layout[index++];
            }
        };
    }
}
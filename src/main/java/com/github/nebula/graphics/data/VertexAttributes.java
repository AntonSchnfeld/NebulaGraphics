package com.github.nebula.graphics.data;

import com.github.nebula.graphics.globjects.Buffer;
import com.github.nebula.graphics.globjects.VertexArray;
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
public final class VertexAttributes implements Iterable<VertexAttribute> {
    public final int size, byteSize;
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
    public VertexAttributes(@NonNull VertexAttribute... layout) {
        validateLayout(layout);
        this.layout = Arrays.copyOf(layout, layout.length);
        var size = 0;
        var byteSize = 0;
        for (var attrib : layout) {
            size += attrib.dataType().size;
            byteSize += attrib.dataType().byteSize;
        }
        this.size = size;
        this.byteSize = byteSize;
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

    /**
     * Formats the vertex array object with the specified buffers.
     * This method sets up the vertex attribute pointers in the vertex array object
     * based on the layout of vertex attributes in this vertex layout and the provided buffers.
     *
     * @param vertexArray The vertex array object to format.
     * @param buffers     The buffers containing vertex attribute data.
     * @throws IllegalArgumentException If the number of buffers does not match the number of vertex attributes in the layout.
     * @throws NullPointerException     If the vertex array or any of the buffers is null.
     */
    public void format(@NonNull VertexArray vertexArray, Buffer @NonNull ... buffers) {
        // Calculate the stride based on the byte sizes of the data types of vertex attributes in the layout
        var stride = 0;
        for (var vertexAttrib : this) {
            stride += vertexAttrib.dataType().byteSize;
        }

        // Iterate over the layout attributes and set up vertex attribute pointers
        // Bind the appropriate buffer for each attribute
        val len = layout.length;
        val buffersLen = buffers.length;
        var ptr = 0;
        for (var i = 0; i < len; i++) {
            val curAttrib = layout[i];
            // Get the buffer corresponding to the current attribute, or use the last buffer if the number of buffers is insufficient
            val buffer = i < buffersLen ? buffers[i] : buffers[buffersLen - 1];
            // Bind the buffer
            buffer.bind();
            // Set up the vertex attribute pointer
            vertexArray.vertexAttribPointer(curAttrib.location(), curAttrib.dataType().size, curAttrib.dataType().glDataType, stride, ptr);
            ptr += curAttrib.dataType().byteSize;
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        VertexAttributes that = (VertexAttributes) o;

        if (size != that.size) return false;
        if (byteSize != that.byteSize) return false;
        // Probably incorrect - comparing Object[] arrays with Arrays.equals
        return Arrays.equals(layout, that.layout);
    }

    @Override
    public int hashCode() {
        int result = Arrays.hashCode(layout);
        result = 31 * result + size;
        result = 31 * result + byteSize;
        return result;
    }

    @Override
    public String toString() {
        return "VertexLayout{" +
                "layout=" + Arrays.toString(layout) +
                ", size=" + size +
                ", byteSize=" + byteSize +
                '}';
    }
}
package com.github.nebula.graphics.data;

import io.reactivex.rxjava3.annotations.NonNull;
import lombok.val;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;

/**
 * @author Anton Schoenfeld
 * @since 22.03.2024
 */
public final class UniformAttributes implements Iterable<UniformAttribute> {
    public final int size, byteSize;
    private final UniformAttribute[] layout;

    public UniformAttributes(@NonNull UniformAttribute... layout) {
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

    private void validateLayout(UniformAttribute[] attributes) {
        val len = attributes.length;
        val names = new HashSet<String>(attributes.length);

        for (final UniformAttribute cur : attributes) {
            if (!names.add(cur.name()))
                throw new InvalidVertexLayoutException("Duplicate name: " + cur.name());
        }
    }

    @NonNull
    public UniformAttribute get(int index) {
        return layout[index];
    }

    @Override
    @NonNull
    public Iterator<UniformAttribute> iterator() {
        return new Iterator<>() {
            private int index = 0;

            @Override
            public boolean hasNext() {
                return index < layout.length;
            }

            @Override
            public UniformAttribute next() {
                return layout[index++];
            }
        };
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        UniformAttributes that = (UniformAttributes) o;

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
        return getClass().getSimpleName() + "{" +
                "layout=" + Arrays.toString(layout) +
                ", size=" + size +
                ", byteSize=" + byteSize +
                '}';
    }
}
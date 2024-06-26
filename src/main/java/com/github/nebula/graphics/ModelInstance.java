package com.github.nebula.graphics;

import io.reactivex.rxjava3.annotations.NonNull;
import lombok.Getter;
import org.joml.Matrix4f;

/**
 * @author Anton Schoenfeld
 * @since 26.03.2024
 */
@Getter
public class ModelInstance implements AutoCloseable {
    private final Model model;
    private final Matrix4f transformationMatrix;

    protected ModelInstance(@NonNull Model model) {
        this.model = model;
        transformationMatrix = new Matrix4f();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ModelInstance that = (ModelInstance) o;

        if (!model.equals(that.model)) return false;
        return transformationMatrix.equals(that.transformationMatrix);
    }

    @Override
    public int hashCode() {
        int result = model.hashCode();
        result = 31 * result + transformationMatrix.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return STR."""
                \{getClass().getSimpleName()}{
                    model=\{model},
                    transformationMatrix=\{transformationMatrix}
                }""";
    }

    @Override
    public void close() {
        model.getInstances().remove(this);
    }
}

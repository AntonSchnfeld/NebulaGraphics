package com.github.nebula.graphics;

import com.github.nebula.graphics.data.GLDataType;
import com.github.nebula.graphics.globjects.Buffer;
import io.reactivex.rxjava3.annotations.NonNull;
import lombok.Getter;
import lombok.val;

import java.nio.FloatBuffer;
import java.util.*;

import static org.lwjgl.opengl.GL15C.*;
import static org.lwjgl.opengl.GL30C.GL_MAP_WRITE_BIT;
import static org.lwjgl.opengl.GL43C.GL_SHADER_STORAGE_BUFFER;

/**
 * @author Anton Schoenfeld
 * @since 26.03.2024
 */
public class Model implements AutoCloseable {
    private final List<Model> children;
    private static final int MAT4F_SIZE = 16;
    private final @Getter List<Mesh> meshes;
    private final List<ModelInstance> instances;
    private final Buffer mat4Ssbo;
    private int numInstances;

    public Model(@NonNull List<Mesh> meshes) {
        super();
        children = new ArrayList<>();
        this.mat4Ssbo = new Buffer(GL_SHADER_STORAGE_BUFFER);
        this.meshes = meshes;
        this.numInstances = 0;
        this.instances = new ArrayList<>();
    }

    protected List<ModelInstance> getInstances() {
        return instances;
    }

    public ModelInstance createInstance() {
        val instance = new ModelInstance(this);
        instances.add(instance);
        uploadTransformationMatrices();
        return instance;
    }

    public void createInstances(ModelInstance[] instances) {
        for (int i = 0; i < instances.length; i++) {
            ModelInstance instance = new ModelInstance(this);
            instances[i] = instance;
            this.instances.add(instance);
        }
        uploadTransformationMatrices();
    }

    private void uploadTransformationMatrices() {
        FloatBuffer mappedMat4Ssbo = mat4Ssbo.mapRange(GL_MAP_WRITE_BIT, 0, instances.size() * MAT4F_SIZE).asFloatBuffer();
        // If new instances were added or instances were removed, we reupload the data
        // so that mat4Ssbo can store all transformation matrices and does not take
        // up unnecessary space.
        if (numInstances != instances.size()) {
            // Get new size and allocate data on GPU
            numInstances = instances.size();
            long size = (long) numInstances * MAT4F_SIZE;
            mat4Ssbo.data(size, GL_DYNAMIC_DRAW, GLDataType.MAT4);
            // Upload transformation matrices to mat4Ssbo
            // Allocate array once and reuse to prevent array creation in loop
            float[] mat4Arr = new float[MAT4F_SIZE];
            for (int i = 0; i < numInstances; i++) {
                // Upload transformation matrix of current instance and mark it as clean
                // since it has just been uploaded
                ModelInstance instance = instances.get(i);
                mappedMat4Ssbo.put(i * MAT4F_SIZE, instance.getTransformationMatrix().get(mat4Arr));
                instance.dirty = false;
            }
        }

        // If any transformation matrices are dirty, we reupload them
        float[] mat4Arr = new float[MAT4F_SIZE];
        for (int i = 0; i < numInstances; i++) {
            ModelInstance instance = instances.get(i);
            if (instance.dirty) {
                mappedMat4Ssbo.put(i * MAT4F_SIZE, instance.getTransformationMatrix().get(mat4Arr));
                instance.dirty = false;
            }
        }

        mat4Ssbo.unmap();
    }

    public void renderInstances() {
        // Reupload transformation matrices if new instances were added
        uploadTransformationMatrices();
        // TODO: Upload vertices and indices of meshMaterialMap into concatMesh
        for (Mesh mesh : meshes) {
            Material mat = mesh.material;
        }
    }

    private boolean isDirty() {
        for (Mesh mesh : meshes) {
            if (mesh.dirty) return true;
        }
        return false;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Model model = (Model) o;

        if (!meshes.equals(model.meshes)) return false;
        return instances.equals(model.instances);
    }

    @Override
    public int hashCode() {
        int result = children.hashCode();
        result = 31 * result + meshes.hashCode();
        result = 31 * result + instances.hashCode();
        result = 31 * result + mat4Ssbo.hashCode();
        result = 31 * result + numInstances;
        return result;
    }

    @Override
    public String toString() {
        return "Model{" +
                "children=" + children +
                ", meshes=" + meshes +
                ", instances=" + instances +
                ", mat4Ssbo=" + mat4Ssbo +
                ", numInstances=" + numInstances +
                '}';
    }

    @Override
    public void close() {
        for (Mesh mesh : meshes) {
            mesh.close();
            mesh.material.close();
        }
        mat4Ssbo.close();
    }
}

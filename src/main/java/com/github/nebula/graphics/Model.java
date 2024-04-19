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
    private static final int MAT4F_SIZE = 16;
    private final @Getter Map<Material, List<Mesh>> meshMaterialMap;
    private final List<ModelInstance> instances;
    private final Buffer mat4Ssbo;
    private GPUMesh concatMesh;
    private int oldNumInstances;

    public Model(@NonNull Map<Material, List<Mesh>> modelMaterialMap) {
        super();
        this.mat4Ssbo = new Buffer(GL_SHADER_STORAGE_BUFFER);
        this.meshMaterialMap = new HashMap<>(modelMaterialMap);
        this.oldNumInstances = 0;
        this.instances = new ArrayList<>();
    }

    protected List<ModelInstance> getInstances() {
        return instances;
    }

    public ModelInstance createInstance() {
        val instance = new ModelInstance(this);
        instances.add(instance);
        return instance;
    }

    public void renderInstances() {
        // Reupload transformation matrices if new instances were added
        // TODO: Handle dynamically changing transformation matrices in ModelInstances
        // TODO: e.g. when a meteroid is moving continously, the transformation matrix
        // TODO: has to be reuploaded
        if (oldNumInstances != instances.size()) {
            oldNumInstances = instances.size();
            long size = (long) oldNumInstances * MAT4F_SIZE;
            mat4Ssbo.data(size, GL_DYNAMIC_DRAW, GLDataType.MAT4);

            FloatBuffer mappedMat4Ssbo = mat4Ssbo.mapRange(GL_MAP_WRITE_BIT, 0, (int) size).asFloatBuffer();
            for (int i = 0; i < oldNumInstances; i++) {
                ModelInstance instance = instances.get(i);
                mappedMat4Ssbo.put(0, instance.getTransformationMatrix().get(new float[MAT4F_SIZE]));
            }
        }
        // TODO: Upload vertices and indices of meshMaterialMap into concatMesh
    }

    private boolean isDirty() {
        for (List<Mesh> meshes : meshMaterialMap.values())
            for (Mesh mesh : meshes)
                if (mesh.dirty) return true;
        return false;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Model model = (Model) o;

        if (!meshMaterialMap.equals(model.meshMaterialMap)) return false;
        return instances.equals(model.instances);
    }

    @Override
    public int hashCode() {
        int result = meshMaterialMap.hashCode();
        result = 31 * result + instances.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return STR."""
                \{getClass().getSimpleName()}{
                    meshMaterialMap=\{meshMaterialMap},
                    instances=\{instances}
                }
                """;
    }

    @Override
    public void close() {
        for (val material : meshMaterialMap.keySet())
            material.close();
        for (val meshList : meshMaterialMap.values())
            meshList.stream().parallel().forEach(Mesh::close);
        mat4Ssbo.close();
    }
}

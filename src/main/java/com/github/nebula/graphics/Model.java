package com.github.nebula.graphics;

import com.github.nebula.graphics.util.BufferUtil;
import io.reactivex.rxjava3.annotations.NonNull;
import lombok.Getter;
import lombok.val;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Anton Schoenfeld
 * @since 26.03.2024
 */
public class Model implements AutoCloseable {
    private final @Getter Map<Material, List<Mesh>> meshMaterialMap;
    private final List<ModelInstance> instances;

    public Model(@NonNull Map<Material, List<Mesh>> modelMaterialMap) {
        super();
        this.meshMaterialMap = modelMaterialMap;
        this.instances = new ArrayList<>();
    }

    public Model() {
        this(new HashMap<>());
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
        for (val material : meshMaterialMap.keySet()) {
            val meshes = meshMaterialMap.get(material);
            val resultMesh = BufferUtil.concatMeshes(new GPUMesh(), meshes.toArray(new Mesh[meshes.size() - 1]));

        }
    }

    public void render() {

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
    }
}

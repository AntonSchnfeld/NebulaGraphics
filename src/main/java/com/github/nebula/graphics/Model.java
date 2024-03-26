package com.github.nebula.graphics;

import io.reactivex.rxjava3.annotations.NonNull;
import lombok.Getter;
import lombok.val;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class Model implements AutoCloseable {
    private final @Getter Map<Mesh, Material> meshMaterialMap;
    private final List<ModelInstance> instances;

    public Model(@NonNull Map<Mesh, Material> modelMaterialMap) {
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Model model = (Model) o;

        return meshMaterialMap.equals(model.meshMaterialMap);
    }

    @Override
    public int hashCode() {
        return meshMaterialMap.hashCode();
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
        for (val mesh : meshMaterialMap.keySet())
            mesh.close();
        for (val material : meshMaterialMap.values())
            material.close();
    }
}

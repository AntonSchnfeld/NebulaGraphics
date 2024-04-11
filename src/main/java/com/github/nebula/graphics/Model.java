package com.github.nebula.graphics;

import com.github.nebula.graphics.data.GLDataType;
import com.github.nebula.graphics.globjects.Buffer;
import com.github.nebula.graphics.globjects.VertexArray;
import com.github.nebula.graphics.util.BufferUtil;
import io.reactivex.rxjava3.annotations.NonNull;
import lombok.Getter;
import lombok.val;
import org.lwjgl.opengl.GL42C;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.lwjgl.opengl.GL15C.*;
import static org.lwjgl.opengl.GL43C.GL_SHADER_STORAGE_BUFFER;

/**
 * @author Anton Schoenfeld
 * @since 26.03.2024
 */
public class Model implements AutoCloseable {
    private final @Getter Map<Material, List<Mesh>> meshMaterialMap;
    private final List<ModelInstance> instances;
    private final Buffer mat4Ssbo;
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
        if (oldNumInstances != instances.size()) {
            oldNumInstances = instances.size();
            mat4Ssbo.data(oldNumInstances, GL_DYNAMIC_DRAW, GLDataType.MAT4);
        }

        val mappedSsbo = mat4Ssbo.map(GL_WRITE_ONLY).asFloatBuffer();
        // Populate Ssbo with transformation matrices
        for (int i = 0; i < instances.size(); i++) {
            val transformMatrix = instances.get(i).getTransformationMatrix();
            mappedSsbo.put(i * 16, transformMatrix.get(new float[16]));
        }
        mat4Ssbo.unmap();

        for (val material : meshMaterialMap.keySet()) {
            val meshes = meshMaterialMap.get(material);
            val resultMesh = BufferUtil.concatMeshesIntoMesh(new GPUMesh(), meshes.toArray(new Mesh[meshes.size() - 1]));
            resultMesh.setIndices(BufferUtil.getDefaultIndexBuffer((int) resultMesh.getVerticesSize()));
            val vao = new VertexArray();
            material.getShader().getVertexAttributes().format(vao, resultMesh.getVbo());
            // TODO: Upload mat4Ssbo into the shader
            vao.bind();
            resultMesh.getEbo().bind();
            material.bind();
            GL42C.glDrawElementsInstanced(GL_TRIANGLES, (int) resultMesh.getIndicesSize(), GL_UNSIGNED_INT, 0L, oldNumInstances);
            vao.close();
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
        mat4Ssbo.close();
    }
}

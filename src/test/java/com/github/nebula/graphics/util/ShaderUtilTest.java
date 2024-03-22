package com.github.nebula.graphics.util;

import com.github.nebula.graphics.data.GLDataType;
import com.github.nebula.graphics.data.UniformAttribute;
import com.github.nebula.graphics.data.VertexAttribute;
import lombok.val;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import java.util.EnumSet;

class ShaderUtilTest {

    @Test
    void parseVertexLayout_given_differentSpacings() {
        val vertexAttribDeclarations = """
                layout(location=0) in vec3 vPos;
                layout (location =1) in vec4 vCol;
                layout( location= 2 ) in vec3 vNorm;
                layout  (location =  3)  in  vec2 vUv;
                """;
        val layout = ShaderUtil.parseVertexAttributes(vertexAttribDeclarations);
        val vPosDecl = layout.get(0);
        Assertions.assertEquals(vPosDecl, new VertexAttribute(GLDataType.VEC3, "vPos", 0));
        val vColDecl = layout.get(1);
        Assertions.assertEquals(vColDecl, new VertexAttribute(GLDataType.VEC4, "vCol", 1));
        val vNormDecl = layout.get(2);
        Assertions.assertEquals(vNormDecl, new VertexAttribute(GLDataType.VEC3, "vNorm", 2));
        val vUvDecl = layout.get(3);
        Assertions.assertEquals(vUvDecl, new VertexAttribute(GLDataType.VEC2, "vUv", 3));
    }

    @Test
    void parseVertexLayout_given_DifferentTypes() {
        val dataTypes = EnumSet.allOf(GLDataType.class);
        val stringBuilder = new StringBuilder();
        {
            val dataTypeIterator = dataTypes.iterator();
            for (var i = 0; i < dataTypes.size(); i++) {
                val dataType = dataTypeIterator.next();
                stringBuilder.append("layout(location=").append(i).append(") in ").append(dataType).
                        append(" val").append(i).append(";");
            }
        }
        val vertexAttribDecls = stringBuilder.toString();

        val uniforms = ShaderUtil.parseVertexAttributes(vertexAttribDecls);
        val dataTypeIterator = dataTypes.iterator();
        for (val uniform : uniforms) {
            Assertions.assertEquals(uniform.dataType(), dataTypeIterator.next());
        }
    }

    @Test
    void parseUniforms_given_differentSpacings() {
        val vertexAttribDeclarations = """
                uniform vec3 vPos;
                uniform vec4 vCol;
                uniform vec3 vNorm;
                uniform vec2 vUv;
                """;
        val layout = ShaderUtil.parseUniformAttributes(vertexAttribDeclarations);
        val vPosDecl = layout.get(0);
        Assertions.assertEquals(vPosDecl, new UniformAttribute(GLDataType.VEC3, "vPos"));
        val vColDecl = layout.get(1);
        Assertions.assertEquals(vColDecl, new UniformAttribute(GLDataType.VEC4, "vCol"));
        val vNormDecl = layout.get(2);
        Assertions.assertEquals(vNormDecl, new UniformAttribute(GLDataType.VEC3, "vNorm"));
        val vUvDecl = layout.get(3);
        Assertions.assertEquals(vUvDecl, new UniformAttribute(GLDataType.VEC2, "vUv"));
    }

    @Test
    void parseUniforms_given_DifferentTypes() {
        val dataTypes = EnumSet.allOf(GLDataType.class);
        val stringBuilder = new StringBuilder();
        {
            val dataTypeIterator = dataTypes.iterator();
            for (var i = 0; i < dataTypes.size(); i++) {
                val dataType = dataTypeIterator.next();
                stringBuilder.append("uniform ").append(dataType.name).append(" val").append(i).append(";").append("\n");
            }
        }
        val uniformAttribDecls = stringBuilder.toString();

        val uniforms = ShaderUtil.parseUniformAttributes(uniformAttribDecls);
        val dataTypeIterator = dataTypes.iterator();
        for (val uniform : uniforms) {
            Assertions.assertEquals(uniform.dataType(), dataTypeIterator.next());
        }
    }
}
package com.github.nebula.graphics.util;

import com.github.nebula.graphics.data.GLDataType;
import com.github.nebula.graphics.data.VertexAttribute;
import lombok.val;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class ShaderUtilTest {

    @Test
    void parseVertexLayout_given_differentSpacings() {
        val vertexAttribDeclarations = """
                layout(location=0) in vec3 vPos;
                layout (location =1) in vec4 vCol;
                layout( location= 2 ) in vec3 vNorm;
                layout  (location =  3)  in  vec2 vUv;
                """;
        val layout = ShaderUtil.parseVertexLayout(vertexAttribDeclarations);
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
    void parseVertexLayout_given_SamplerTypes() {
        val vertexAttribDeclarations = """
                layout(location = 0) in sampler1D s1d;
                layout(location = 1) in sampler1DArray s1dArr;
                layout(location = 2) in sampler1DShadow s1dShadow;
                layout(location = 3) in sampler1DArrayShadow s1dArrShadow;
                layout(location = 4) in sampler2D s2d;
                layout(location = 5) in sampler2DArray s2dArr;
                layout(location = 6) in sampler2DShadow s2dShadow;
                layout(location = 7) in sampler2DArrayShadow s2dArrShadow;
                layout(location = 8) in sampler2DRect s2dRect;
                layout(location = 9) in sampler2DRectShadow s2dRectShadow;
                layout(location = 10) in sampler3D s3d;
                layout(location = 11) in samplerCube sCube;
                layout(location = 12) in samplerCubeArray sCubeArr;
                layout(location = 13) in samplerCubeArrayShadow sCubeArrShadow;
                layout(location = 14) in samplerBuffer sBuf;
                """;
        val samplerTypes = new GLDataType[]{
                GLDataType.SAMPLER1D,
                GLDataType.SAMPLER1DARRAY,
                GLDataType.SAMPLER1DSHADOW,
                GLDataType.SAMPLER1DARRAYSHADOW,

                GLDataType.SAMPLER2D,
                GLDataType.SAMPLER2DARRAY,
                GLDataType.SAMPLER2DSHADOW,
                GLDataType.SAMPLER2DARRAYSHADOW,

                GLDataType.SAMPLER2DRECT,
                GLDataType.SAMPLER2DRECTSHADOW,

                GLDataType.SAMPLER3D,

                GLDataType.SAMPLERCUBE,
                GLDataType.SAMPLERCUBEARRAY,
                GLDataType.SAMPLERCUBEARRAYSHADOW,

                GLDataType.SAMPLERBUFFER
        };

        val layout = ShaderUtil.parseVertexLayout(vertexAttribDeclarations);
        val layoutIterator = layout.iterator();
        for (GLDataType samplerType : samplerTypes) {
            Assertions.assertEquals(layoutIterator.next().dataType(), samplerType);
        }
    }
}
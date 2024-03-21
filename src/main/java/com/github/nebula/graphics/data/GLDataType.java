package com.github.nebula.graphics.data;

import lombok.AllArgsConstructor;

import static org.lwjgl.opengl.GL42C.*;

/**
 * Represents data types used in GLSL (OpenGL Shading Language).
 * Each enum constant corresponds to a specific GLSL data type.
 *
 * @author Anton Schoenfeld
 * @since 21.03.2024
 */
@AllArgsConstructor
public enum GLDataType {
    FLOAT("float", Float.BYTES, 1, GL_FLOAT),
    INT("int", Integer.BYTES, 1, GL_INT),
    SHORT("short", Short.BYTES, 1, GL_SHORT),
    DOUBLE("double", Double.BYTES, 1, GL_DOUBLE),

    VEC2("vec2", Float.BYTES * 2, 2, GL_FLOAT),
    VEC3("vec3", Float.BYTES * 3, 3, GL_FLOAT),
    VEC4("vec4", Float.BYTES * 4, 4, GL_FLOAT),

    IVEC2("ivec2", Integer.BYTES * 2, 2, GL_INT),
    IVEC3("ivec3", Integer.BYTES * 3, 3, GL_INT),
    IVEC4("ivec4", Integer.BYTES * 4, 4, GL_INT),

    MAT2("mat2", Float.BYTES * 4, 4, GL_FLOAT),
    MAT3("mat3", Float.BYTES * 9, 9, GL_FLOAT),
    MAT4("mat4", Float.BYTES * 16, 16, GL_FLOAT),

    DMAT2("dmat2", Double.BYTES * 4, 4, GL_DOUBLE),
    DMAT3("dmat3", Double.BYTES * 9, 9, GL_DOUBLE),
    DMAT4("dmat4", Double.BYTES * 16, 16, GL_DOUBLE),

    SAMPLER1D("sampler1D", Integer.BYTES, 1, GL_SAMPLER_1D),
    SAMPLER2D("sampler2D", Integer.BYTES, 1, GL_SAMPLER_2D),
    SAMPLER3D("sampler3D", Integer.BYTES, 1, GL_SAMPLER_3D),
    SAMPLERCUBE("samplerCube", Integer.BYTES, 1, GL_SAMPLER_CUBE),
    SAMPLER1DSHADOW("sampler1DShadow", Integer.BYTES, 1, GL_SAMPLER_1D_SHADOW),
    SAMPLER2DSHADOW("sampler2DShadow", Integer.BYTES, 1, GL_SAMPLER_2D_SHADOW),
    SAMPLER2DRECT("sampler2DRect", Integer.BYTES, 1, GL_SAMPLER_2D_RECT),
    SAMPLER2DRECTSHADOW("sampler2DRectShadow", Integer.BYTES, 1, GL_SAMPLER_2D_RECT_SHADOW),
    SAMPLERCUBESHADOW("samplerCubeShadow", Integer.BYTES, 1, GL_SAMPLER_CUBE_SHADOW),
    SAMPLERBUFFER("samplerBuffer", Integer.BYTES, 1, GL_SAMPLER_BUFFER),
    SAMPLER1DARRAY("sampler1DArray", Integer.BYTES, 1, GL_SAMPLER_1D_ARRAY),
    SAMPLER2DARRAY("sampler2DArray", Integer.BYTES, 1, GL_SAMPLER_2D_ARRAY),
    SAMPLER1DARRAYSHADOW("sampler1DArrayShadow", Integer.BYTES, 1, GL_SAMPLER_1D_ARRAY_SHADOW),
    SAMPLER2DARRAYSHADOW("sampler2DArrayShadow", Integer.BYTES, 1, GL_SAMPLER_2D_ARRAY_SHADOW),
    SAMPLERCUBEARRAY("samplerCubeArray", Integer.BYTES, 1, GL_SAMPLER_CUBE_MAP_ARRAY),
    SAMPLERCUBEARRAYSHADOW("samplerCubeArrayShadow", Integer.BYTES, 1, GL_SAMPLER_CUBE_MAP_ARRAY_SHADOW);

    public final String name;
    public final int byteSize;
    public final int size;
    public final int glDataType;
}

package com.github.nebula.graphics.util;

import com.github.nebula.graphics.data.*;
import lombok.val;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.regex.Pattern;

/**
 * Utility class providing methods for shader-related operations.
 * This class assists in parsing vertex layout declarations from vertex shader source code.
 *
 * @author Anton Schoenfeld
 * @since 21.03.2024
 */
public class ShaderUtil {

    private static final Pattern VERTEX_LAYOUT_DECL =
            Pattern.compile("\\blayout\\s*\\(\\s*location\\s*=\\s*(\\d+)\\s*\\)\\s*in\\s+(\\w+)\\s+(\\w+)\\s*;");
    private static final Pattern UNIFORM_LAYOUT_DECL_REGEX =
            Pattern.compile("\\s*\\buniform\\s+(\\w+)\\s+(\\w+)\\s*;");

    /**
     * Parses the vertex layout declarations from the provided vertex shader source code.
     * Each declaration specifies the layout of a vertex attribute within the vertex shader.
     *
     * @param vertexSource The source code of the vertex shader.
     * @return The vertex layout extracted from the shader source.
     * @throws IllegalArgumentException If the shader source code is invalid or contains unrecognized data types.
     */
    public static VertexAttributes parseVertexAttributes(String vertexSource) {
        val vertexSourceLines = vertexSource.split("\n");

        // Gets all lines where a vertex attribute is declared
        // Example:     layout(location = 0) in vec2 vPos;
        // Example 2:   layout (location= 1) in vec4  vCol;

        val vertexAttribList = new ArrayList<VertexAttribute>();
        for (val line : vertexSourceLines) {
            val matcher = VERTEX_LAYOUT_DECL.matcher(line);
            if (matcher.matches()) {
                val loc = Integer.parseInt(matcher.group(1));
                val dataType = matcher.group(2);
                val attribName = matcher.group(3);
                try {
                    vertexAttribList.add(new VertexAttribute(GLDataType.valueOf(dataType.toUpperCase()), attribName, loc));
                } catch (IllegalArgumentException e) {
                    throw new IllegalArgumentException("Unrecognized vertex attribute data type: " + dataType);
                }
            }
        }

        return new VertexAttributes(vertexAttribList.toArray(new VertexAttribute[0]));
    }

    public static UniformAttributes parseUniformAttributes(String... shaders) {
        val lines = new ArrayList<String>();
        for (val shader : shaders)
            lines.addAll(Arrays.asList(shader.split("\n")));

        val uniformAttribList = new ArrayList<UniformAttribute>();

        for (val line : lines) {
            val matcher = UNIFORM_LAYOUT_DECL_REGEX.matcher(line);
            if (matcher.matches()) {
                val dataType = matcher.group(1);
                val attribName = matcher.group(2);
                try {
                    uniformAttribList.add(new UniformAttribute(GLDataType.valueOf(dataType.toUpperCase()), attribName));
                } catch (IllegalArgumentException e) {
                    throw new IllegalArgumentException("Unrecognized uniform attribute data type: " + dataType);
                }
            }
        }

        return new UniformAttributes(uniformAttribList.toArray(new UniformAttribute[0]));
    }
}

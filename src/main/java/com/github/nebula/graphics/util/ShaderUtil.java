package com.github.nebula.graphics.util;

import com.github.nebula.graphics.data.GLSLDataType;
import com.github.nebula.graphics.data.VertexAttribute;
import com.github.nebula.graphics.data.VertexLayout;
import lombok.val;

import java.util.ArrayList;
import java.util.regex.Pattern;

/**
 * Utility class providing methods for shader-related operations.
 * This class assists in parsing vertex layout declarations from vertex shader source code.
 *
 * @author Anton Schoenfeld
 * @since 21.03.2024
 */
public class ShaderUtil {

    /**
     * Parses the vertex layout declarations from the provided vertex shader source code.
     * Each declaration specifies the layout of a vertex attribute within the vertex shader.
     *
     * @param vertexSource The source code of the vertex shader.
     * @return The vertex layout extracted from the shader source.
     * @throws IllegalArgumentException If the shader source code is invalid or contains unrecognized data types.
     */
    public static VertexLayout parseVertexLayout(String vertexSource) {
        val vertexSourceLines = vertexSource.split(System.lineSeparator());

        // Gets all lines where a vertex attribute is declared
        // Example:     layout(location = 0) in vec2 vPos;
        // Example 2:   layout (location= 1) in vec4  vCol;
        val vertexLayoutDeclarationPattern = Pattern.compile("\\blayout\\s*\\(\\s*location\\s*=\\s*(\\d+)\\s*\\)\\s+in\\s+(\\w+)\\s+(\\w+);");

        val vertexAttribList = new ArrayList<VertexAttribute>();
        for (val line : vertexSourceLines) {
            val matcher = vertexLayoutDeclarationPattern.matcher(line);
            if (matcher.matches()) {
                val loc = Integer.parseInt(matcher.group(1));
                val dataType = matcher.group(2);
                val attribName = matcher.group(3);
                try {
                    vertexAttribList.add(new VertexAttribute(GLSLDataType.valueOf(dataType.toUpperCase()), attribName, loc));
                } catch (IllegalArgumentException e) {
                    throw new IllegalArgumentException("Unrecognized data type: " + dataType);
                }
            }
        }

        return new VertexLayout(vertexAttribList.toArray(new VertexAttribute[0]));
    }
}

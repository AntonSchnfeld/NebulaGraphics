package com.github;

import com.github.nebula.graphics.globjects.*;
import com.github.nebula.graphics.globjects.texture.TextureConfig;
import com.github.nebula.graphics.globjects.texture.TextureDimensions;
import com.github.nebula.graphics.globjects.texture.TextureFilter;
import com.github.nebula.graphics.window.Window;
import com.github.nebula.graphics.window.WindowHints;
import lombok.val;
import org.joml.Vector2i;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.opengl.GL11C;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL33C.*;

public class Main {
    public static void main(String[] args) {
        var windowHints = new WindowHints().defaultHints();
        try (val window = new Window(windowHints, Main.class.getName())) {
            window.createGLCapabilities();
            test(window);
        }
    }

    private static void test(Window window) {
        int width = window.getSize().x;
        int height = window.getSize().y;
        try (val vao = new VertexArray();
             val vbo = new Buffer(GL_ARRAY_BUFFER);
             val triangleTimeColorShader = new Shader("""
                     #version 330 core
                     layout(location = 0) in vec2 vPos;
                     void main() {
                         gl_Position = vec4(vPos, 0, 1);
                     }
                     """, """
                     #version 330 core
                     out vec4 FragColor;
                     uniform float time;
                     void main() {
                         FragColor = vec4(sin(time), sin(time + 50), sin(time + 100), 1);
                     }
                     """);
             val postProcessColorInvertShader = new Shader("""
                     #version 330 core
                     layout(location = 0) in vec2 vPos;
                     layout(location = 1) in vec2 vUv;
                     out vec2 fUv;
                     void main() {
                         fUv = vUv;
                         gl_Position = vec4(vPos, 0, 1);
                     }
                     """, """
                     #version 330 core
                     in vec2 fUv;
                     uniform sampler2D uScreen;
                     uniform float time;
                     out vec4 FragCol;
                     void main() {
                         FragCol = 1 - texture(uScreen, fUv);
                     }
                     """);
             val mainRenderTarget = new FrameBuffer();
             val mainRenderTargetColorAttachment = new Texture(new TextureDimensions(width, height), new TextureFilter(GL_NEAREST, GL_NEAREST), new TextureConfig());
             val mainRenderTargetDepthStencilAttachment = new RenderBuffer(GL_DEPTH24_STENCIL8, width, height);
             val vertexArray = new VertexArray();
             val postProcessingVbo = new Buffer(GL_ARRAY_BUFFER);
             val postProcessingEbo = new Buffer(GL_ELEMENT_ARRAY_BUFFER)) {

            float[] vertices = new float[]{
                    -0.5f, -0.5f,
                    0.5f, -0.5f,
                    0f, 0.5f
            };
            vbo.data(vertices, GL_STATIC_DRAW);

            vbo.bind();
            vao.vertexAttribPointer(0, 2, GL_FLOAT, 2 * Float.BYTES, 0);
            triangleTimeColorShader.bind();

            mainRenderTarget.attach(mainRenderTargetColorAttachment, GL_COLOR_ATTACHMENT0);
            mainRenderTarget.attach(mainRenderTargetDepthStencilAttachment, GL_DEPTH_STENCIL_ATTACHMENT);
            mainRenderTarget.complete();

            float[] fullScreenVertices = {
                    -1, -1, 0, 0,
                    -1, 1, 0, 1,
                    1, 1, 1, 1,
                    1, -1, 1, 0,
            };
            postProcessingVbo.data(fullScreenVertices, GL_STATIC_DRAW);
            int[] indices = {
                    0, 1, 2,
                    0, 2, 3,
            };
            postProcessingEbo.data(indices, GL_STATIC_DRAW);
            postProcessingVbo.bind();
            vertexArray.vertexAttribPointer(0, 2, GL_FLOAT, 4 * Float.BYTES, 0);
            vertexArray.vertexAttribPointer(1, 2, GL_FLOAT, 4 * Float.BYTES, 2 * Float.BYTES);
            postProcessingVbo.unbind();

            final Vector2i windowSize = new Vector2i();
            final Vector2i windowPos = new Vector2i();
            window.setRenderListener(() -> {
                if (GLFW.glfwGetKey(window.getId(), GLFW_KEY_ESCAPE) == GLFW_PRESS) {
                    glfwSetWindowShouldClose(window.getId(), true);
                }
                mainRenderTarget.bind();
                window.getSize(windowSize);
                window.getPosition(windowPos);
                glClearColor(0, 0, 0, 0);
                glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
                glViewport(0, 0, windowSize.x, windowSize.y);
                triangleTimeColorShader.bind();
                triangleTimeColorShader.uploadUniformFloat("time", (float) glfwGetTime());
                vao.bind();
                glDrawArrays(GL11C.GL_TRIANGLES, 0, 3);
                mainRenderTarget.unbind();

                glClearColor(0, 0, 0, 1);
                glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
                postProcessColorInvertShader.bind();
                mainRenderTargetColorAttachment.bindToSlot(0);
                postProcessColorInvertShader.uploadUniformInt("uScreen", 0);
                postProcessColorInvertShader.uploadUniformFloat("time", (float) glfwGetTime());
                vertexArray.bind();
                postProcessingEbo.bind();
                glDrawElements(GL_TRIANGLES, indices.length, GL_UNSIGNED_INT, 0);
            });

            window.loop();
        }
    }
}
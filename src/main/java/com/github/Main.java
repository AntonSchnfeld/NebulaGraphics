package com.github;

import com.github.nebula.graphics.globjects.*;
import com.github.nebula.graphics.globjects.texture.TextureDimensions;
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
        try (val vao = new VertexArray();
             val vbo = new Buffer(GL_ARRAY_BUFFER);
             val shader = new Shader("""
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
             val shader2 = new Shader("""
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
             val fbo = new FrameBuffer();
             val texture = new Texture(new TextureDimensions(window.getSize().x, window.getSize().y));
             val rbo = new RenderBuffer(GL_DEPTH24_STENCIL8, window.getSize().x, window.getSize().y);
             val vertexArray = new VertexArray();
             val buffer = new Buffer(GL_ARRAY_BUFFER);
             val elementBuffer = new Buffer(GL_ELEMENT_ARRAY_BUFFER)) {

            float[] vertices = new float[]{
                    -0.5f, -0.5f,
                    0.5f, -0.5f,
                    0f, 0.5f
            };
            vbo.data(vertices, GL_STATIC_DRAW);

            vbo.bind();
            vao.vertexAttribPointer(0, 2, GL_FLOAT, 2 * Float.BYTES, 0);
            shader.bind();

            fbo.attach(texture, GL_COLOR_ATTACHMENT0);
            fbo.attach(rbo, GL_DEPTH_STENCIL_ATTACHMENT);
            fbo.complete();

            float[] fullScreenVertices = {
                    -1, -1, 0, 0,
                    -1, 1, 0, 1,
                    1, 1, 1, 1,
                    1, -1, 1, 0
            };
            buffer.data(fullScreenVertices, GL_STATIC_DRAW);
            int[] indices = {
                    0, 1, 2,
                    0, 2, 3
            };
            elementBuffer.data(indices, GL_STATIC_DRAW);
            buffer.bind();
            vertexArray.vertexAttribPointer(0, 2, GL_FLOAT, 4 * Float.BYTES, 0);
            vertexArray.vertexAttribPointer(1, 2, GL_FLOAT, 4 * Float.BYTES, 2 * Float.BYTES);
            buffer.unbind();

            final Vector2i windowSize = new Vector2i();
            final Vector2i windowPos = new Vector2i();
            window.setRenderListener(() -> {
                if (GLFW.glfwGetKey(window.getId(), GLFW_KEY_ESCAPE) == GLFW_PRESS) {
                    glfwSetWindowShouldClose(window.getId(), true);
                }
                fbo.bind();
                window.getSize(windowSize);
                window.getPosition(windowPos);
                glClearColor(0, 0, 0, 0);
                glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
                glViewport(0, 0, windowSize.x, windowSize.y);
                shader.bind();
                shader.uploadUniformFloat("time", (float) glfwGetTime());
                vao.bind();
                glDrawArrays(GL11C.GL_TRIANGLES, 0, 3);
                fbo.unbind();

                glClearColor(0, 0, 0, 1);
                glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
                shader2.bind();
                texture.bindToSlot(0);
                shader2.uploadUniformInt("uScreen", 0);
                shader2.uploadUniformFloat("time", (float) glfwGetTime());
                vertexArray.bind();
                elementBuffer.bind();
                glDrawElements(GL_TRIANGLES, indices.length, GL_UNSIGNED_INT, 0);
            });

            window.loop();
        }
    }
}
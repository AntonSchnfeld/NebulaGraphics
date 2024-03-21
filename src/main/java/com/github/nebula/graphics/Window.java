package com.github.nebula.graphics;

import com.github.nebula.graphics.data.ByteBufferedImage;
import org.joml.Vector2i;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.glfw.GLFWImage;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GLCapabilities;
import org.lwjgl.system.MemoryStack;

import java.nio.IntBuffer;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11C.*;
import static org.lwjgl.system.MemoryStack.stackPush;
import static org.lwjgl.system.MemoryUtil.NULL;

public class Window implements AutoCloseable {
    public static final int DEFAULT_WIDTH = 500;
    public static final int DEFAULT_HEIGHT = 500;
    private long windowObject;
    private Runnable renderListener;
    private GLFWErrorCallback errorCallback;
    private ByteBufferedImage currentIcon;
    private String title;
    private boolean resizable;

    public Window(final String title, final int x, final int y, final int width, final int height) {
        if (!glfwInit())
            throw new IllegalStateException("Could not initialize GLFW library");

        this.title = title;
        resizable = true;
        init(title, x, y, width, height);
    }

    public Window(final String title, final int width, final int height) {
        this(title, 0, 0,
                width, height);
        center();
    }

    public Window(final String title) {
        this(title, DEFAULT_WIDTH, DEFAULT_HEIGHT);
    }

    public void setRenderListener(Runnable renderListener) {
        this.renderListener = renderListener;
    }

    private void init(String title, int x, int y, int width, int height) {
        // Set up an error callback. The default implementation
        // will print the error message in System.err.
        renderListener = () -> {
        };
        errorCallback = GLFWErrorCallback.createPrint(System.err);
        errorCallback.set();

        // Configure GLFW
        glfwDefaultWindowHints(); // optional, the current window hints are already the default
        glfwWindowHint(GLFW_VISIBLE, GLFW_TRUE); // the window will stay hidden after creation
        glfwWindowHint(GLFW_RESIZABLE, GLFW_TRUE); // the window will be non-resizable
        glfwWindowHint(GLFW_OPENGL_DEBUG_CONTEXT, GLFW_TRUE);

        // Create the window
        windowObject = glfwCreateWindow(width, height, title, NULL, NULL);
        if (windowObject == NULL)
            throw new RuntimeException("Failed to create GLFW window");

        // Get the thread stack and push a new frame
        try (MemoryStack stack = stackPush()) {
            IntBuffer pWidth = stack.mallocInt(1); // int*
            IntBuffer pHeight = stack.mallocInt(1); // int*

            // Get the window size passed to glfwCreateWindow
            glfwGetWindowSize(windowObject, pWidth, pHeight);

            // Get the resolution of the primary monitor
            GLFWVidMode vidMode = glfwGetVideoMode(glfwGetPrimaryMonitor());

            // Center the window
            glfwSetWindowPos(windowObject, (vidMode.width() - pWidth.get(0)) / 2,
                    (vidMode.height() - pHeight.get(0)) / 2);
        } // the stack frame is popped automatically

        // Make the OpenGL context current
        glfwMakeContextCurrent(windowObject);
        // Enable v-sync
        glfwSwapInterval(1);

        setPosition(x, y);

        glfwWindowHint(GLFW_OPENGL_PROFILE, GLFW_OPENGL_CORE_PROFILE);

        glfwShowWindow(windowObject);
    }

    public GLCapabilities createGLCapabilities() {
        return GL.createCapabilities();
    }

    public void loop() {
        long now;
        long then = System.currentTimeMillis();
        int frame = 0;

        Vector2i size = new Vector2i();
        while (!glfwWindowShouldClose(windowObject)) {
            getSize(size);
            glfwSwapBuffers(windowObject);
            glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
            glViewport(0, 0, size.x, size.y);
            glClearColor(0, 0, 0, 1);

            renderListener.run();
            frame++;
            now = System.currentTimeMillis();
            if (now - then >= 1000) {
                glfwSetWindowTitle(windowObject, title + " FPS: " + frame);
                frame = 0;
                then = System.currentTimeMillis();
            }

            // Poll for window events. The key callback above will only be invoked during this call.
            glfwPollEvents();
        }
    }

    public void center() {
        GLFWVidMode vidMode = glfwGetVideoMode(glfwGetPrimaryMonitor());
        if (vidMode == null)
            throw new IllegalStateException("Could not retrieve glfw vid mode");

        Vector2i size = getSize();

        setPosition(((vidMode.width() - size.x) / 2), ((vidMode.height() - size.y) / 2));
    }

    public boolean isResizable() {
        return resizable;
    }

    public void setSize(int width, int height) {
        glfwSetWindowSize(windowObject, width, height);
    }

    public void setPosition(int x, int y) {
        glfwSetWindowPos(windowObject, x, y);
    }

    public void setWindowIcon(ByteBufferedImage icon) {
        // Dispose of previous icon if it exists
        if (currentIcon != null) currentIcon.close();
        currentIcon = icon;

        // Allocate native resources
        GLFWImage.Buffer imageBuffer = GLFWImage.malloc(1);
        GLFWImage glfwImage = GLFWImage.malloc();
        glfwImage.set(icon.width(), icon.height(), icon.bytes());
        imageBuffer.put(0, glfwImage);

        // Seg window icon
        glfwSetWindowIcon(windowObject, imageBuffer);

        // Free allocated resources
        imageBuffer.free();
        glfwImage.free();
    }

    public Vector2i getSize() {
        return getSize(new Vector2i());
    }

    public Vector2i getSize(Vector2i vector) {
        try (MemoryStack stack = stackPush()) {
            IntBuffer width = stack.mallocInt(1);
            IntBuffer height = stack.mallocInt(1);

            glfwGetWindowSize(windowObject, width, height);

            vector.x = width.get();
            vector.y = height.get();
        }
        return vector;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
        glfwSetWindowTitle(windowObject, title);
    }

    public Vector2i getPosition() {
        return getPosition(new Vector2i());
    }

    public Vector2i getPosition(Vector2i position) {
        try (MemoryStack stack = stackPush()) {
            IntBuffer x = stack.mallocInt(1);
            IntBuffer y = stack.mallocInt(1);

            glfwGetWindowPos(windowObject, x, y);

            position.x = x.get();
            position.y = y.get();
        }

        return position;
    }

    @Override
    public void close() {
        if (currentIcon != null) currentIcon.close();
        errorCallback.free();
        glfwDestroyWindow(windowObject);
        glfwTerminate();
    }
}
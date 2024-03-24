package com.github.nebula.graphics.window;

import lombok.AllArgsConstructor;

import static org.lwjgl.glfw.GLFW.*;

@AllArgsConstructor
public enum WindowHint {
    RESIZABLE(GLFW_RESIZABLE),
    VISIBLE(GLFW_VISIBLE),
    DECORATED(GLFW_DECORATED),
    FOCUSED(GLFW_FOCUSED),
    AUTO_ICONIFY(GLFW_AUTO_ICONIFY),
    FLOATING(GLFW_FLOATING),
    MAXIMIZED(GLFW_MAXIMIZED),
    CENTER_CURSOR(GLFW_CENTER_CURSOR),
    TRANSPARENT_FRAMEBUFFER(GLFW_TRANSPARENT_FRAMEBUFFER),
    FOCUS_ON_SHOW(GLFW_FOCUS_ON_SHOW),
    MOUSE_PASSTHROUGH(GLFW_MOUSE_PASSTHROUGH),
    STEREO(GLFW_STEREO),
    SRGB_CAPABLE(GLFW_SRGB_CAPABLE),
    DOUBLEBUFFER(GLFW_DOUBLEBUFFER),
    CONTEXT_DEBUG(GLFW_CONTEXT_DEBUG),
    CONTEXT_NO_ERROR(GLFW_CONTEXT_NO_ERROR);

    public final int glfwConstant;
}
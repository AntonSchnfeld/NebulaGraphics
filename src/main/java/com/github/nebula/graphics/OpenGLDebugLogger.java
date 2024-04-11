package com.github.nebula.graphics;

import io.reactivex.rxjava3.annotations.NonNull;
import org.lwjgl.opengl.GL43;
import org.lwjgl.opengl.GLDebugMessageCallbackI;
import org.lwjgl.system.MemoryUtil;

import java.util.logging.Logger;

/**
 * @author Anton Schoenfeld
 * @since 03.04.2024
 */
public class OpenGLDebugLogger implements GLDebugMessageCallbackI {
    private static final Logger LOGGER = Logger.getLogger(OpenGLDebugLogger.class.getName());

    public OpenGLDebugLogger() {
        LOGGER.info("OpenGL Debug Logging enabled");
    }

    @NonNull
    private static String getDebugSourceString(int source) {
        return switch (source) {
            case GL43.GL_DEBUG_SOURCE_API -> "API";
            case GL43.GL_DEBUG_SOURCE_WINDOW_SYSTEM -> "Window System";
            case GL43.GL_DEBUG_SOURCE_SHADER_COMPILER -> "Shader Compiler";
            case GL43.GL_DEBUG_SOURCE_THIRD_PARTY -> "Third Party";
            case GL43.GL_DEBUG_SOURCE_APPLICATION -> "Application";
            case GL43.GL_DEBUG_SOURCE_OTHER -> "Other";
            default -> "Unknown";
        };
    }

    @NonNull
    private static String getDebugTypeString(int type) {
        return switch (type) {
            case GL43.GL_DEBUG_TYPE_ERROR -> "Error";
            case GL43.GL_DEBUG_TYPE_DEPRECATED_BEHAVIOR -> "Deprecated Behavior";
            case GL43.GL_DEBUG_TYPE_UNDEFINED_BEHAVIOR -> "Undefined Behavior";
            case GL43.GL_DEBUG_TYPE_PORTABILITY -> "Portability";
            case GL43.GL_DEBUG_TYPE_PERFORMANCE -> "Performance";
            case GL43.GL_DEBUG_TYPE_OTHER -> "Other";
            case GL43.GL_DEBUG_TYPE_MARKER -> "Marker";
            default -> "Unknown";
        };
    }

    @NonNull
    private static String getDebugSeverityString(int severity) {
        return switch (severity) {
            case GL43.GL_DEBUG_SEVERITY_HIGH -> "High";
            case GL43.GL_DEBUG_SEVERITY_MEDIUM -> "Medium";
            case GL43.GL_DEBUG_SEVERITY_LOW -> "Low";
            case GL43.GL_DEBUG_SEVERITY_NOTIFICATION -> "Notification";
            default -> "Unknown";
        };
    }

    @Override
    public void invoke(int source, int type, int id, int severity, int length, long message, long userParam) {
        String sourceStr = getDebugSourceString(source);
        String typeStr = getDebugTypeString(type);
        String severityStr = getDebugSeverityString(severity);
        String messageStr = MemoryUtil.memUTF8(message);

        LOGGER.info(STR."""
                        OpenGL Debug Message:
                            Source: \{sourceStr},
                            Type: \{typeStr},
                            ID: \{id},
                            Severity: \{severityStr},
                            Message: \{messageStr}
                        """);
    }
}

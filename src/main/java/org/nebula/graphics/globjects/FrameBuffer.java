package org.nebula.graphics.globjects;

import lombok.Getter;
import org.nebula.graphics.globjects.exceptions.FrameBufferNotCompleteException;

import java.util.HashMap;
import java.util.Map;

import static org.lwjgl.opengl.GL33C.*;

/**
 * The {@code FrameBuffer} class provides an abstraction for working with framebuffers in OpenGL.
 * It encapsulates framebuffer creation, attachment of textures and renderbuffers, completeness checking,
 * and resource management.
 *
 * <p>Instances of this class represent framebuffer objects, which are used for off-screen rendering
 * and advanced rendering techniques in OpenGL applications.
 *
 * <p>The class offers methods for attaching textures and renderbuffers to the framebuffer, checking
 * framebuffer completeness, and binding/unbinding the framebuffer to the OpenGL context.
 *
 * <p>Framebuffers created with this class can be used for various purposes, including rendering to textures,
 * depth buffers, or stencil buffers. The class abstracts the complexity of working with framebuffers
 * in OpenGL, providing a high-level interface for common framebuffer operations.
 *
 * <p>When a framebuffer instance is no longer needed, it should be closed using the {@link #close()} method
 * to release OpenGL resources associated with it.
 *
 * <p>Example usage:
 * <pre>{@code
 * FrameBuffer frameBuffer = new FrameBuffer();
 * frameBuffer.attachTexture(texture, GL_COLOR_ATTACHMENT0, 0);
 * frameBuffer.attachRenderBuffer(renderBuffer, GL_DEPTH_ATTACHMENT);
 * frameBuffer.complete();
 * }</pre>
 *
 * @author Anton Schoenfeld
 * @since 12.03.2024
 */
@Getter
public class FrameBuffer extends OpenGLObject {
    /**
     * Indicates whether the framebuffer is complete.
     */
    private boolean complete;

    /**
     * A map of attachment points to attached OpenGL objects (textures or renderbuffers).
     */
    private final Map<Integer, OpenGLObject> attachments;

    /**
     * Constructs a FrameBuffer object, creating a new framebuffer in OpenGL.
     */
    public FrameBuffer() {
        super(glGenFramebuffers());

        attachments = new HashMap<>();
        complete = false;
    }

    /**
     * Attaches a texture to the framebuffer.
     *
     * @param texture    The texture to attach.
     * @param attachment The attachment point (e.g., GL_COLOR_ATTACHMENT0).
     * @param mipMapLevel The mipmap level of the texture.
     */
    public void attachTexture(Texture texture, int attachment, int mipMapLevel) {
        attachments.put(attachment, texture);

        bind();
        glFramebufferTexture2D(GL_FRAMEBUFFER, attachment, GL_TEXTURE_2D, texture.id, mipMapLevel);
        unbind();
    }

    /**
     * Attaches a renderbuffer to the framebuffer.
     *
     * @param renderBuffer The renderbuffer to attach.
     * @param attachment   The attachment point (e.g., GL_DEPTH_ATTACHMENT).
     */
    public void attachRenderBuffer(RenderBuffer renderBuffer, int attachment) {
        attachments.put(attachment, renderBuffer);

        bind();
        glFramebufferRenderbuffer(GL_FRAMEBUFFER, attachment, GL_RENDERBUFFER, renderBuffer.id);
        unbind();
    }

    /**
     * Checks if the framebuffer is complete.
     * Throws a FrameBufferNotCompleteException if the framebuffer is not complete.
     */
    public void complete() {
        if (!complete) {
            bind();
            if (glCheckFramebufferStatus(GL_FRAMEBUFFER) != GL_FRAMEBUFFER_COMPLETE) {
                throw new FrameBufferNotCompleteException("Framebuffer status is not complete");
            }
            complete = false;
            unbind();
        }
    }

    /**
     * Binds the framebuffer to the OpenGL context.
     */
    public void bind() {
        glBindFramebuffer(GL_FRAMEBUFFER, id);
    }

    /**
     * Unbinds the framebuffer from the OpenGL context.
     */
    public void unbind() {
        glBindFramebuffer(GL_FRAMEBUFFER, 0);
    }

    /**
     * Releases OpenGL resources associated with the framebuffer.
     */
    @Override
    public void close() {
        glDeleteFramebuffers(id);
    }
}
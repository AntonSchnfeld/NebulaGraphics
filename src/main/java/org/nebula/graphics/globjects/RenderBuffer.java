package org.nebula.graphics.globjects;

import lombok.Getter;

import javax.swing.*;
import javax.swing.text.DefaultFormatterFactory;

import static org.lwjgl.opengl.GL33C.*;

/**
 * The {@code RenderBuffer} class represents an OpenGL Renderbuffer object used for off-screen rendering,
 * particularly in conjunction with framebuffers. This class extends {@link org.nebula.graphics.globjects.OpenGLObject}, encapsulating
 * the creation, binding, and destruction of OpenGL Renderbuffers.
 *
 * <p>A Renderbuffer is a storage object for framebuffer attachments that store pixel data, such as color,
 * depth, or stencil information. Instances of this class are created with specified dimensions, internal
 * format, and optional multisampling parameters.
 *
 * <p>This class provides flexibility with two constructors: one for multisampled Renderbuffers and one for
 * non-multisampled Renderbuffers. The Renderbuffer is automatically generated and bound during construction,
 * and the associated OpenGL resources are released when the instance is closed, making it compatible with
 * the try-with-resources statement.
 *
 * <p>Instances of this class should be used in conjunction with {@link FrameBuffer} to define the
 * attachments of framebuffers for rendering purposes.
 *
 * @author Anton Schoenfeld
 * @since 10.03.2024
 */
@Getter
public final class RenderBuffer extends OpenGLObject implements FrameBufferAttachment
{

    /**
     * The width of the Renderbuffer in pixels.
     */
    private final int width;

    /**
     * The height of the Renderbuffer in pixels.
     */
    private final int height;

    /**
     * The internal format of the Renderbuffer (e.g., GL_RGBA8, GL_DEPTH_COMPONENT, etc.).
     */
    private final int internalFormat;

    /**
     * Constructs a new Renderbuffer with multisampling support.
     *
     * @param samples        The number of samples for multisampling.
     * @param internalFormat The internal format of the Renderbuffer.
     * @param width          The width of the Renderbuffer in pixels.
     * @param height         The height of the Renderbuffer in pixels.
     */
    public RenderBuffer(int samples, int internalFormat, int width, int height) {
        super(glGenRenderbuffers());
        this.width = width;
        this.height = height;
        this.internalFormat = internalFormat;

        bind();
        glRenderbufferStorageMultisample(GL_RENDERBUFFER, samples, internalFormat, width, height);
        unbind();
    }

    /**
     * Constructs a new Renderbuffer without multisampling.
     *
     * @param internalFormat The internal format of the Renderbuffer.
     * @param width          The width of the Renderbuffer in pixels.
     * @param height         The height of the Renderbuffer in pixels.
     */
    public RenderBuffer(int internalFormat, int width, int height) {
        this(0, internalFormat, width, height);
    }

    /**
     * Binds the Renderbuffer, making it the active Renderbuffer for subsequent OpenGL operations.
     */
    @Override
    public void bind() {
        glBindRenderbuffer(GL_RENDERBUFFER, id);
    }

    /**
     * Unbinds the currently bound Renderbuffer, making no Renderbuffer active.
     */
    @Override
    public void unbind() {
        glBindRenderbuffer(GL_RENDERBUFFER, 0);
    }

    /**
     * Deletes the OpenGL Renderbuffer Object associated with this RenderBuffer.
     */
    @Override
    public void close() {
        glDeleteRenderbuffers(id);
    }
}
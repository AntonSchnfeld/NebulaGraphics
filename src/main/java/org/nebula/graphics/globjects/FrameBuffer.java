package org.nebula.graphics.globjects;

import lombok.Getter;

import static org.lwjgl.opengl.GL33C.*;

@Getter
public class FrameBuffer extends OpenGLObject {
    private final Texture texture;
    private final int renderBuffer;

    public FrameBuffer(int width, int height) {
        super(glGenFramebuffers());

        glBindFramebuffer(GL_FRAMEBUFFER, id);

        texture = new Texture(new TextureDimensions(width, height));

        glFramebufferTexture2D(GL_FRAMEBUFFER, GL_COLOR_ATTACHMENT0, GL_TEXTURE_2D, texture.id, 0);
        texture.unbind();

        this.renderBuffer = glGenRenderbuffers();
        glBindRenderbuffer(GL_RENDERBUFFER, renderBuffer);
        glRenderbufferStorage(GL_RENDERBUFFER, GL_DEPTH24_STENCIL8, width, height);

        glFramebufferRenderbuffer(GL_FRAMEBUFFER, GL_DEPTH_STENCIL_ATTACHMENT, GL_RENDERBUFFER, renderBuffer);
        glBindRenderbuffer(GL_RENDERBUFFER, 0);

        if (glCheckFramebufferStatus(GL_FRAMEBUFFER) != GL_FRAMEBUFFER_COMPLETE)
            throw new IllegalStateException("FrameBuffer construction was not completed | FrameBuffer status: " + glCheckFramebufferStatus(GL_FRAMEBUFFER));

        unbind();
    }

    public void bind() {
        glBindFramebuffer(GL_FRAMEBUFFER, id);
    }

    public void unbind() {
        glBindFramebuffer(GL_FRAMEBUFFER, 0);
    }

    @Override
    public void close() {
        glDeleteFramebuffers(id);
        texture.close();
        glDeleteRenderbuffers(renderBuffer);
    }
}

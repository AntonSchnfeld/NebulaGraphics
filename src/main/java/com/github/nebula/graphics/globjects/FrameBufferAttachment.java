package com.github.nebula.graphics.globjects;

/**
 * Marker interface for frame buffer attachments.
 * This interface designates objects that can be attached to a frame buffer, such as textures or render buffers.
 * @author Anton Schoenfeld
 * @since 12.03.2024
 */
public sealed interface FrameBufferAttachment permits Texture, RenderBuffer {}
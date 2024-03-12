package org.nebula.graphics.globjects.exceptions;

public class FrameBufferNotCompleteException extends RuntimeException {
    public FrameBufferNotCompleteException() {
        super();
    }

    public FrameBufferNotCompleteException(String msg) {
        super(msg);
    }
}

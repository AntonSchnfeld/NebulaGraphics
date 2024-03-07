package org.nebula.graphics.globjects.exceptions;

public class ShaderException extends RuntimeException {
    public ShaderException(String msg) {
        super(msg);
    }

    public ShaderException() {
        super();
    }

    public ShaderException(Throwable cause) {
        super(cause);
    }
}

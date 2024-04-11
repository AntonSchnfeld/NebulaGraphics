module com.github.nebula.graphics {
    exports com.github.nebula.graphics;
    exports com.github.nebula.graphics.globjects;
    exports com.github.nebula.graphics.globjects.texture;
    exports com.github.nebula.graphics.globjects.exceptions;
    exports com.github.nebula.graphics.data;
    exports com.github.nebula.graphics.util;
    exports com.github.nebula.graphics.window;

    requires transitive lombok;
    requires transitive io.reactivex.rxjava3;
    requires transitive java.logging;

    requires org.joml;
    requires org.lwjgl;
    requires org.lwjgl.opengl;
    requires org.lwjgl.glfw;
    requires org.lwjgl.stb;
}
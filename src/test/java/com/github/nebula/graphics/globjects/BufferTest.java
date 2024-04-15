package com.github.nebula.graphics.globjects;

import com.github.nebula.graphics.window.Window;
import com.github.nebula.graphics.window.WindowHint;
import com.github.nebula.graphics.window.WindowHints;
import lombok.val;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;
import static org.lwjgl.opengl.GL43C.*;

class BufferTest implements AutoCloseable {

    private static final float[] DATA = {
        0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19,
    };

    /**
     * Exists just for OpenGL context
     */
    private Window contextHolder;
    private Buffer buffer;

    @BeforeEach
    public void setup() {
        // Enable debug
        System.setProperty("-debug", Boolean.TRUE.toString());
        // glfwDefaultHints();
        val wh = new WindowHints().defaultHints();
        // Disable window visibility to prevent windows popping up during testing
        wh.windowHint(WindowHint.VISIBLE, false);
        contextHolder = new Window(wh, getClass().getName());
        // Create GL context
        contextHolder.createGLCapabilities();
        // Create our Buffer and initialize it with test data
        buffer = new Buffer(GL_ARRAY_BUFFER);
        buffer.data(DATA, GL_DYNAMIC_READ);
    }

    @Test
    public void map_given_Read() {
        val byteBuf = buffer.map(GL_READ_ONLY);
        val floatBuf = byteBuf.asFloatBuffer();

        for (int i = 0; i < DATA.length; i++) {
            assertEquals(DATA[i], floatBuf.get(i));
        }

        buffer.unmap();
    }

    @Test
    public void map_given_Write() {
        ByteBuffer byteBuf = buffer.map(GL_WRITE_ONLY);
        FloatBuffer floatBuf = byteBuf.asFloatBuffer();

        float[] newData = new float[DATA.length];
        Arrays.fill(newData, 256);
        floatBuf.put(0, newData, 0, newData.length);

        buffer.unmap();

        byteBuf = buffer.map(GL_READ_ONLY);
        floatBuf = byteBuf.asFloatBuffer();

        for (int i = 0; i < newData.length; i++) {
            assertEquals(newData[i], floatBuf.get(i));
        }

        buffer.unmap();
    }

    @Test
    public void map_given_ReadWrite() {
        ByteBuffer byteBuf = buffer.map(GL_READ_WRITE);
        FloatBuffer floatBuf = byteBuf.asFloatBuffer();

        float[] newData = new float[DATA.length];
        Arrays.fill(newData, 256);
        floatBuf.put(0, newData, 0, newData.length);

        buffer.unmap();

        byteBuf = buffer.map(GL_READ_WRITE);
        floatBuf = byteBuf.asFloatBuffer();

        for (int i = 0; i < newData.length; i++) {
            assertEquals(newData[i], floatBuf.get(i));
        }

        buffer.unmap();
    }

    @Test
    public void mapRange_given_MapReadBit() {
        // Define boundaries of range
        int startIdx = 1;
        int endIdx = DATA.length - 1;
        int len = (endIdx - startIdx) * Float.BYTES;

        ByteBuffer byteBuf = buffer.mapRange(GL_MAP_READ_BIT, startIdx * Float.BYTES, len);

        // Confirm that the required range size was mapped
        assertEquals(len, byteBuf.limit(), "Mapped Buffer does not have length of len");

        // Compare values
        for (int i = startIdx, j = 0; i < endIdx; i++, j++) {
            assertEquals(DATA[i], byteBuf.getFloat(j * Float.BYTES));
        }

        buffer.unmap();
    }

    @Test
    public void mapRange_given_MapWriteBit() {
        int startIdx = 1;
        int endIdx = DATA.length - 1;
        int len = (endIdx - startIdx) * Float.BYTES;

        ByteBuffer byteBuf = buffer.mapRange(GL_MAP_WRITE_BIT, startIdx * Float.BYTES, len);

        assertEquals(len, byteBuf.limit(), "Mapped Buffer does not have length of len");

        float[] newData = new float[len / Float.BYTES];
        Arrays.fill(newData, 256);

        for (int i = 0; i < newData.length; i++)
            byteBuf.putFloat(i * Float.BYTES, newData[i]);

        buffer.unmap();

        byteBuf = buffer.mapRange(GL_MAP_READ_BIT, startIdx * Float.BYTES, len);

        assertEquals(len, byteBuf.limit(), "Mapped Buffer does not have length of len");

        for (int i = 0; i < newData.length; i++) {
            assertEquals(newData[i], byteBuf.getFloat(i * Float.BYTES));
        }

        buffer.unmap();
    }

    @Test
    public void mapRange_given_MapReadWriteBit() {
        int startIdx = 1;
        int endIdx = DATA.length - 1;
        int len = (endIdx - startIdx) * Float.BYTES;

        ByteBuffer byteBuf = buffer.mapRange(GL_MAP_WRITE_BIT | GL_MAP_READ_BIT, startIdx * Float.BYTES, len);

        // Confirm that the required range size was mapped
        assertEquals(len, byteBuf.limit(), "Mapped Buffer does not have length of len");

        // Compare values
        for (int i = startIdx, j = 0; i < endIdx; i++, j++) {
            assertEquals(DATA[i], byteBuf.getFloat(j * Float.BYTES));
        }

        // Write new values
        float[] newData = new float[len / Float.BYTES];
        Arrays.fill(newData, 256);

        for (int i = 0; i < newData.length; i++)
            byteBuf.putFloat(i * Float.BYTES, newData[i]);

        buffer.unmap();

        // Assert that new values have been properly written
        byteBuf = buffer.mapRange(GL_MAP_READ_BIT, startIdx * Float.BYTES, len);

        assertEquals(len, byteBuf.limit(), "Mapped Buffer does not have length of len");

        for (int i = 0; i < newData.length; i++) {
            assertEquals(newData[i], byteBuf.getFloat(i * Float.BYTES));
        }

        buffer.unmap();
    }

    @AfterEach
    @Override
    public void close() {
        buffer.close();
        contextHolder.close();
    }
}
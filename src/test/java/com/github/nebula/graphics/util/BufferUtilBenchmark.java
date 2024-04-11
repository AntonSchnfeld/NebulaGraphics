package com.github.nebula.graphics.util;

import com.github.nebula.graphics.Mesh;
import com.github.nebula.graphics.NativeMesh;
import org.lwjgl.system.MemoryUtil;
import org.openjdk.jmh.Main;
import org.openjdk.jmh.annotations.*;

import java.io.IOException;
import java.nio.FloatBuffer;
import java.util.concurrent.TimeUnit;

public class BufferUtilBenchmark {
    @BenchmarkMode(Mode.AverageTime)
    @OutputTimeUnit(TimeUnit.MILLISECONDS)
    @State(Scope.Thread)
    public static class concatMeshes {
        // Define state variables
        private Mesh[] meshes;
        private Mesh concatMesh;

        public static void main(String[] args) throws IOException {
            String[] jmhArgs = {
                    "-f", "1", // Forks
                    "-wi", "20", // Warmup iterations
                    "-i", "40", // Measurement iterations
                    "-w", "100ms", // Warmup time
                    "-r", "100ms", // Measurement time
                    "-t", "1", // Threads
                    "-tu", "ms", // Time unit
                    ".*BufferUtilBenchmark.concatMeshes.*" // Regex to match your benchmark class
            };

            Main.main(jmhArgs);
        }

        @Setup(Level.Iteration)
        public void setup() {
            // Initialize meshes and concatMesh
            int numMeshes = 10_000; // Number of meshes to concatenate
            meshes = new Mesh[numMeshes];
            for (int i = 0; i < numMeshes; i++) {
                meshes[i] = createMockMesh(); // Create a mock mesh, implement as needed
            }
            concatMesh = new NativeMesh();
        }

        @Benchmark
        public Mesh benchmarkConcatenation() {
            return BufferUtil.concatMeshesIntoMesh(concatMesh, meshes);
        }

        @TearDown(Level.Iteration)
        public void close() {
            for (var mesh : meshes) {
                mesh.close();
            }
            concatMesh.close();
        }

        // Method to create a mock mesh for testing purposes
        private Mesh createMockMesh() {
            var mesh = new NativeMesh();

            var len = 10;
            var vertices = MemoryUtil.memAllocFloat(len);
            var indices = MemoryUtil.memAllocInt(len);

            for (int i = 0; i < len; i++) {
                vertices.put(i);
                indices.put(i);
            }

            mesh.setVertices(vertices);
            mesh.setIndices(indices);

            return mesh;
        }
    }

    @BenchmarkMode(Mode.AverageTime)
    @OutputTimeUnit(TimeUnit.MILLISECONDS)
    @State(Scope.Thread)
    public static class concatFloatBuffers {
        private FloatBuffer[] floatBuffers;

        public static void main(String[] args) throws IOException {
            String[] jmhArgs = {
                    "-f", "1", // Forks
                    "-wi", "20", // Warmup iterations
                    "-i", "40", // Measurement iterations
                    "-w", "50ms", // Warmup time
                    "-r", "50ms", // Measurement time
                    "-t", "1", // Threads
                    "-tu", "ms", // Time unit
                    ".*BufferUtilBenchmark.concatFloatBuffers.d*" // Regex to match your benchmark class
            };

            Main.main(jmhArgs);
        }

        @Setup(Level.Iteration)
        public void setup() {
            var numFloatBuffers = 10_000;
            floatBuffers = new FloatBuffer[numFloatBuffers];
            for (var i = 0; i < numFloatBuffers; i++) {
                floatBuffers[i] = createMockFloatBuffer();
            }
        }

        @Benchmark
        public FloatBuffer benchmarkConcatenation() {
            return BufferUtil.concatFloatBuffers(floatBuffers);
        }

        @TearDown(Level.Iteration)
        public void close() {
            for (var floatBuffer : floatBuffers) {
                MemoryUtil.memFree(floatBuffer);
            }
        }

        private FloatBuffer createMockFloatBuffer() {
            var len = 100;
            var mock = MemoryUtil.memAllocFloat(len);
            for (var i = 0; i < len; i++) {
                mock.put(i);
            }
            return mock;
        }
    }
}

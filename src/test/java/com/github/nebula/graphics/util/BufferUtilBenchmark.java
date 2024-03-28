package com.github.nebula.graphics.util;

import com.github.nebula.graphics.Mesh;
import com.github.nebula.graphics.NativeMesh;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import org.lwjgl.system.MemoryUtil;
import org.openjdk.jmh.Main;
import org.openjdk.jmh.annotations.*;

@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
@State(Scope.Thread)
public class BufferUtilBenchmark {

    // Define state variables
    private Mesh[] meshes;
    private Mesh concatMesh;

    @Setup
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
        return BufferUtil.concatMeshes(concatMesh, meshes);
    }

    @TearDown
    public void close() {
        for (var mesh : meshes) {
            mesh.close();
        }
        concatMesh.close();
    }

    // Method to create a mock mesh for testing purposes
    private Mesh createMockMesh() {
        var mesh = new NativeMesh();

        var len = 1000;
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

    public static void main(String[] args) throws IOException
    {
        String[] jmhArgs = {
                "-f", "1", // Forks
                "-wi", "5", // Warmup iterations
                "-i", "5", // Measurement iterations
                "-w", "2s", // Warmup time
                "-r", "2s", // Measurement time
                "-t", "1", // Threads
                "-tu", "ms", // Time unit
                ".*BufferUtilBenchmark.*" // Regex to match your benchmark class
        };

        Main.main(jmhArgs);
    }
}

package io.benchmarks.models;

import org.openjdk.jmh.annotations.Param;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.State;

@State(Scope.Benchmark)
public class Chunk {
    
    public static final int EVENT_SIZE = 32;
    
    @Param({"32768", "1048576", "33554432", "1073741824", "8589934592"})
    public long chunkSize;
    
    public static final long CHUNK_SIZE_1 = 32768; // 2^15 - 32 Kb
    
    public static final long CHUNK_SIZE_2 = 1048576; // 2^20 - 1 Mb
    
    public static final long CHUNK_SIZE_3 = 33554432; // 2^25 - 33 Mb
    
    public static final long CHUNK_SIZE_4 = 1073741824; // 2^30 - 1 Gb
    
    public static final long CHUNK_SIZE_5 = 8589934592L; // 2^33 - 8 Gb
}

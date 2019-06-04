package io.benchmarks.write.chunks;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Fork;
import org.openjdk.jmh.annotations.Level;
import org.openjdk.jmh.annotations.Measurement;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.annotations.TearDown;
import org.openjdk.jmh.annotations.Warmup;

import io.benchmarks.models.Chunk;
import io.benchmarks.models.Event;

@Fork(value = 1, warmups = 1)
@Warmup(iterations = 1)
@Measurement(iterations = 3)
public class ChunkWriteUsingRandomAccessFile {
    
    @Benchmark
    @BenchmarkMode(Mode.AverageTime)
    public void throughput(Persister persister, Event event, Chunk chunk) {

	writeChunk(persister, event, chunk.chunkSize);
    }
    
    public void writeChunk(Persister persister, Event event, long chunkSize) {
	
	while (persister.position <= chunkSize - 32) {
	    persistEvent(persister, event);
	    persister.position += 32;
	}
    } 
    
    public void persistEvent(Persister persister, Event event) {

	RandomAccessFile raf = persister.randomAccessFile;

	try {
	    raf.writeLong(event.val1);
	    raf.writeLong(event.val2);
	    raf.writeLong(event.val3);
	    raf.writeLong(event.val4);

	} catch (IOException e) {
	    e.printStackTrace();
	}
    }
    
    @State(Scope.Benchmark)
    public static class Persister {

	private final long FILE_SIZE = 8589934592L; // 8,5 Gb 2^33
	
	private final File file = new File("RandomAccess.tst");

	public RandomAccessFile randomAccessFile;
	public long position;

	@Setup(Level.Iteration)
	public void setUp() {
	    
	    try {
		file.delete();
		
		randomAccessFile = new RandomAccessFile(file, "rw");
		randomAccessFile.setLength(FILE_SIZE);
		
		position = 0;

	    } catch (FileNotFoundException e) {
		e.printStackTrace();
		
	    } catch (IOException e) {
		e.printStackTrace();
	    }
	}

	@TearDown(Level.Iteration)
	public void shutdown() {
	     
	    try {
		this.randomAccessFile.setLength(0);
		this.randomAccessFile.close();
		this.file.delete();
		
	    } catch (IOException e) {
		e.printStackTrace();
	    }
	}
    }
}

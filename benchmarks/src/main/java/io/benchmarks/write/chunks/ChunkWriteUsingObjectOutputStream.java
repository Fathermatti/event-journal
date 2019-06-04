package io.benchmarks.write.chunks;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;

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
public class ChunkWriteUsingObjectOutputStream {
    
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

	try {
	    event.writeExternal(persister.objectStream);
	    
	} catch (IOException e) {
	    e.printStackTrace();
	}
    }

    @State(Scope.Benchmark)
    public static class Persister {

	private File file = new File("ObjectOutputStream.tst");
	
	public ObjectOutputStream objectStream;
	public long position;

	@Setup(Level.Invocation)
	public void setUp() {

	    file.delete();

	    try {
		objectStream = new ObjectOutputStream(new BufferedOutputStream(new FileOutputStream(file)));
		position = 0;

	    } catch (IOException e) {
		e.printStackTrace();
	    }
	}

	@TearDown(Level.Invocation)
	public void tearDown() {

	    try {
		objectStream.close();
		file.delete();

	    } catch (IOException e) {
		e.printStackTrace();
	    }
	}
    }
}

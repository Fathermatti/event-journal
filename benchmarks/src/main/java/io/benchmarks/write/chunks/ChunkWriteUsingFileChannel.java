package io.benchmarks.write.chunks;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

import org.openjdk.jmh.annotations.*;

import io.benchmarks.models.Chunk;
import io.benchmarks.models.Event;

@Fork(value = 1, warmups = 1)
@Warmup(iterations = 1)
@Measurement(iterations = 3)
public class ChunkWriteUsingFileChannel {
    
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

	ByteBuffer buffer = persister.byteBuffer;

	buffer.putLong(event.val1);
	buffer.putLong(event.val2);
	buffer.putLong(event.val3);
	buffer.putLong(event.val4);

	if (buffer.position() >= buffer.limit() - 32) {

	    try {
		buffer.flip();
		persister.fileChannel.write(buffer);
		buffer.clear();

	    } catch (IOException e) {
		e.printStackTrace();
	    }
	}
    }

    @State(Scope.Benchmark)
    public static class Persister {

	public int BUFFER_SIZE = 8192;
	public File file = new File("FileChannel.tst");
	
	public FileOutputStream outputStream;
	public FileChannel fileChannel;
	public ByteBuffer byteBuffer;
	public long position;

	@Setup(Level.Invocation)
	public void setUp() {

	    file.delete();
	    
	    byteBuffer = ByteBuffer.allocateDirect(BUFFER_SIZE);
	    position = 0; 

	    try {
		outputStream = new FileOutputStream(file);
		fileChannel = outputStream.getChannel();

	    } catch (FileNotFoundException e) {
		e.printStackTrace();
	    }
	}

	@TearDown(Level.Invocation)
	public void teardown() {

	    try {
		fileChannel.close();
		outputStream.close();
		file.delete();

	    } catch (IOException e) {
		e.printStackTrace();
	    }
	}
    }
}

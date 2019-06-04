package io.benchmarks.channel;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

import org.openjdk.jmh.annotations.*;

import io.benchmarks.models.Event;

@Fork(value = 1, warmups = 1)
@Warmup(iterations = 1)
@Measurement(iterations = 3)
public class FileChannelBufferSize {
    
    @Benchmark
    @BenchmarkMode(Mode.Throughput)
    public void throughput(Persister persister, Event event) {
	
	persistEvent(persister, event);
    }

    @Benchmark
    @BenchmarkMode(Mode.SampleTime)
    public void latency(Persister persister, Event event) {
	
	persistEvent(persister, event);
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

	// @Param({ "4096", "8192", "32768", "65536", "262144", "1048576"})
	@Param({ "536870912"})
	public int BUFFER_SIZE;

	public File file = new File("FileChannel.tst");

	public FileOutputStream outputStream;
	public FileChannel fileChannel;
	public ByteBuffer byteBuffer;

	@Setup(Level.Iteration)
	public void setUp() {
	    
	    file.delete();
	    byteBuffer = ByteBuffer.allocateDirect(BUFFER_SIZE);

	    try {
		outputStream = new FileOutputStream(file);
		fileChannel = outputStream.getChannel();

	    } catch (FileNotFoundException e) {
		e.printStackTrace();
	    }
	}

	@TearDown(Level.Iteration)
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

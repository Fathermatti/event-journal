package io.benchmarks.write.continuous;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;

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

import io.benchmarks.models.Event;

@Fork(value = 1, warmups = 1)
@Warmup(iterations = 1)
@Measurement(iterations = 3)
public class ContinuousWriteUsingBufferedStream {

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

	BufferedOutputStream outputStream = persister.bufferedOutputStream;

	persister.byteBuffer.putLong(event.val1);
	persister.byteBuffer.putLong(event.val2);
	persister.byteBuffer.putLong(event.val3);
	persister.byteBuffer.putLong(event.val4);
	
	byte[] byteArray = persister.byteBuffer.array();
	
	try {
	    outputStream.write(byteArray);
	    persister.byteBuffer.clear();

	} catch (IOException e) {
	    e.printStackTrace();
	}
    }

    @State(Scope.Benchmark)
    public static class Persister {

	private File file = new File("BufferedStream.tst");
	public byte[] byteArray = new byte[32];
	
	public ByteBuffer byteBuffer;
	public BufferedOutputStream bufferedOutputStream;

	@Setup(Level.Iteration)
	public void setUp() {
	    
	    file.delete();

	    try {
		byteBuffer = ByteBuffer.wrap(byteArray);
		bufferedOutputStream = new BufferedOutputStream(new FileOutputStream(file));

	    } catch (FileNotFoundException e) {
		e.printStackTrace();
	    }
	}

	@TearDown(Level.Iteration)
	public void tearDown() {
	    
	    try {
		bufferedOutputStream.close();
		file.delete();

	    } catch (IOException e) {
		e.printStackTrace();
	    }
	}
    }
}

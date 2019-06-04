
package io.benchmarks.write.continuous;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.FileChannel.MapMode;

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
public class ContinuousWriteUsingMappedByteArray {

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

	MappedByteBuffer buffer = persister.mappedByteBuffer;

	if (buffer.position() >= buffer.limit() - 100) {
	    mapFileChannel(persister);
	}

	buffer.putLong(event.val1);
	buffer.putLong(event.val2);
	buffer.putLong(event.val3);
	buffer.putLong(event.val4);
    }

    public void mapFileChannel(Persister persister) {

	try {
	    persister.mappedByteBuffer = persister.fileChannel.map(MapMode.READ_WRITE, persister.position,
		    persister.MAP_SIZE);

	    persister.position += persister.MAP_SIZE;
	    persister.mapCount += 1;

	} catch (IOException e) {
	    e.printStackTrace();
	}
    }

    @State(Scope.Benchmark)
    public static class Persister {

	public final int MAP_SIZE = 2147483647; // 2 Gb 2^30
	public final long FILE_SIZE = 8589934592L; // 8,5 Gb 2^33

	public File file = new File("MemoryMapped.tst");

	public long position;
	public int mapCount;

	public RandomAccessFile randomAccessFile;
	public FileChannel fileChannel;
	public MappedByteBuffer mappedByteBuffer;

	@Setup(Level.Iteration)
	public void setUp() {

	    try {
		file.delete();

		randomAccessFile = new RandomAccessFile(file, "rw");
		randomAccessFile.setLength(FILE_SIZE);

		fileChannel = randomAccessFile.getChannel();

		mappedByteBuffer = fileChannel.map(MapMode.READ_WRITE, 0, MAP_SIZE);
		position = MAP_SIZE;
		mapCount = 1;

	    } catch (FileNotFoundException e) {
		e.printStackTrace();

	    } catch (IOException e) {
		e.printStackTrace();
	    }
	}

	@TearDown(Level.Iteration)
	public void tearDown() {

	    System.out.println("\nMap has been used: " + this.mapCount + " times.");

	    try {
		this.randomAccessFile.setLength(0);
		this.fileChannel.close();
		this.randomAccessFile.close();
		this.file.delete();

	    } catch (IOException e) {
		e.printStackTrace();
	    }
	}
    }

}

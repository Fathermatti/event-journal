package io.benchmarks.write.chunks;

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

import io.benchmarks.models.Chunk;
import io.benchmarks.models.Event;

@Fork(value = 1, warmups = 1)
@Warmup(iterations = 1)
@Measurement(iterations = 3)
public class ChunkWriteUsingMappedByteBuffer {

    @Benchmark
    @BenchmarkMode(Mode.AverageTime)
    public void throughput(Persister persister, Event event, Chunk chunk) {

	writeChunk(persister, event, chunk.chunkSize);
    }

    @Benchmark
    @BenchmarkMode(Mode.SingleShotTime)
    public void singleShot(Persister persister, Event event, Chunk chunk) {

	writeChunk(persister, event, chunk.chunkSize);
    }

    public void writeChunk(Persister persister, Event event, long chunkSize) {

	while (persister.positionInChunk <= chunkSize - 32) {
	    persistEvent(persister, event);
	    persister.positionInChunk += 32;
	}
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
	    persister.mappedByteBuffer = persister.fileChannel.map(MapMode.READ_WRITE, persister.positionInFile,
		    persister.MAP_SIZE);

	    persister.positionInFile += persister.MAP_SIZE;
	    persister.mapCount += 1;

	} catch (IOException e) {
	    e.printStackTrace();
	}
    }

    @State(Scope.Benchmark)
    public static class Persister {

	private File file = new File("MemoryMapped.tst");
	private int MAP_SIZE = Integer.MAX_VALUE;

	public RandomAccessFile randomAccessFile;
	public FileChannel fileChannel;
	public MappedByteBuffer mappedByteBuffer;
	public long positionInFile;
	public long positionInChunk;
	public int mapCount;

	@Setup(Level.Invocation)
	public void setUp() {

	    try {
		file.delete();

		randomAccessFile = new RandomAccessFile(file, "rw");
		randomAccessFile.setLength(10737418240L);

		fileChannel = randomAccessFile.getChannel();

		mappedByteBuffer = fileChannel.map(MapMode.READ_WRITE, 0, MAP_SIZE);
		mappedByteBuffer.clear();

		positionInFile = MAP_SIZE;
		positionInChunk = 0;
		mapCount = 1;

	    } catch (FileNotFoundException e) {
		e.printStackTrace();

	    } catch (IOException e) {
		e.printStackTrace();
	    }

	}

	@TearDown(Level.Iteration)
	public void tearDown() {

	    try {
		randomAccessFile.setLength(0);
		fileChannel.close();
		randomAccessFile.close();
		mappedByteBuffer = null;
		file.delete();

	    } catch (IOException e) {
		e.printStackTrace();
	    }
	}
    }
}

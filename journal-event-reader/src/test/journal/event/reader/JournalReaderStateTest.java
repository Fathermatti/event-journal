package journal.event.reader;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.FileNotFoundException;
import java.io.RandomAccessFile;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.easymock.EasyMock.*;

public class JournalReaderStateTest {

    private RandomAccessFile file;
    private MappedByteBuffer map;

    @Before
    public void setUp() throws Exception {
        Path directory = Files.createDirectories(Paths.get("testing"));
        Path filePath = directory.resolve("test.jnl");

        try {
            file = new RandomAccessFile(filePath.toFile(), "rw");
            map = file.getChannel()
                    .map(FileChannel.MapMode.READ_WRITE, 0, 100);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void getEventTest() {

        EventBufferReader bufferReader = mock(EventBufferReader.class);
        JournalReaderMemoryMapper memoryMapper = mock(JournalReaderMemoryMapper.class);
        JournalReaderFiles readerFiles = mock(JournalReaderFiles.class);

        expect(memoryMapper.getNextMappedBuffer()).andReturn(map).once();
        expect(bufferReader.getEvent(map)).andReturn(new Event()).once();

        map.position(0);
        map.limit(100);

        replay(memoryMapper, bufferReader);

        JournalReadState state = new JournalReadState(bufferReader, memoryMapper, readerFiles);
        state.getNextEvent();

        verify(memoryMapper, bufferReader);
    }

    @Test
    public void getNewMapAndGetEventTest() {

        EventBufferReader bufferReader = mock(EventBufferReader.class);
        JournalReaderMemoryMapper memoryMapper = mock(JournalReaderMemoryMapper.class);
        JournalReaderFiles readerFiles = mock(JournalReaderFiles.class);

        expect(memoryMapper.getNextMappedBuffer()).andReturn(map).times(2);
        expect(bufferReader.getEvent(map)).andReturn(new Event()).once();

        map.position(100);
        map.limit(100);

        replay(memoryMapper, bufferReader);

        JournalReadState state = new JournalReadState(bufferReader, memoryMapper, readerFiles);
        state.getNextEvent();

        verify(memoryMapper, bufferReader);
    }

    @After
    public void tearDown() throws Exception {
        file.close();
    }
}

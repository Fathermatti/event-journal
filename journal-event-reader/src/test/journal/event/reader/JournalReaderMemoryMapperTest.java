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
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class JournalReaderMemoryMapperTest {

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
    public void hasRemainingBytesTest() {

        RandomAccessFile file = mock(RandomAccessFile.class);
        JournalReaderFiles readerFiles = mock(JournalReaderFiles.class);

        expect(readerFiles.getNextFile()).andReturn(file).once();
        expect(readerFiles.getFileLength(file)).andReturn(100L).once();

        replay(readerFiles);

        JournalReaderMemoryMapper memoryMapper = new JournalReaderMemoryMapper(readerFiles);
        boolean value = memoryMapper.hasRemainingBytes();

        assertEquals(true, value);
    }

    @Test
    public void getNextJournalBufferTest() {

        JournalReaderFiles readerFiles = mock(JournalReaderFiles.class);

        expect(readerFiles.getNextFile()).andReturn(file).once();
        expect(readerFiles.getFileLength(file)).andReturn(100L).once();

        replay(readerFiles);

        JournalReaderMemoryMapper memoryMapper = new JournalReaderMemoryMapper(readerFiles);
        MappedByteBuffer journalBuffer = memoryMapper.getNextMappedBuffer();

        assertNotNull(journalBuffer);
    }

    @Test
    public void getNextMappedBufferAndReplaceFileTest() {

        JournalReaderFiles readerFiles = mock(JournalReaderFiles.class);

        expect(readerFiles.getNextFile()).andReturn(file).times(2);
        expect(readerFiles.getFileLength(file)).andReturn(0L).times(2);

        replay(readerFiles);

        JournalReaderMemoryMapper memoryMapper = new JournalReaderMemoryMapper(readerFiles);
        MappedByteBuffer value = memoryMapper.getNextMappedBuffer();

        assertNotNull(value);
    }

    @After
    public void tearDown() throws Exception {
        file.close();
    }
}


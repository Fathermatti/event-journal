package journal.event.reader.integration;

import journal.event.reader.*;
import org.junit.Before;
import org.junit.Test;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.MappedByteBuffer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.easymock.EasyMock.*;
import static org.junit.Assert.assertEquals;

public class JournalReaderIntegrationTest {

    private RandomAccessFile file;

    @Before
    public void setUp() throws Exception {
        // DirectoryUtil.clearAndDeleteDirectory("testing");
        Path directory = Files.createDirectories(Paths.get("testing"));

        Path filePath1 = directory.resolve("test1.jnl");
        Path filePath2 = directory.resolve("test2.jnl");
        Path filePath3 = directory.resolve("test3.jnl");

        fillFile(filePath1, 1);
        fillFile(filePath2, 2);
        fillFile(filePath3, 3);
    }

    private void fillFile(Path path, int factor) {

        try {
            file = new RandomAccessFile(path.toFile(), "rw");

            file.writeLong(1 * factor);
            file.writeInt(2 * factor);
            file.writeDouble(3.0 * factor);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void getEventsFromAllFiles() {

        EventBufferReader bufferReader = new EventBufferReader();

        JournalReader<Event> journalReader = JournalReaderFactory.createJournalReader(bufferReader, "testing");

        Event event1 = journalReader.getNextEvent();
        Event event2 = journalReader.getNextEvent();
        Event event3 = journalReader.getNextEvent();

        assertEquals(1, event1.ask);
        assertEquals(2, event1.bid);
        assertEquals(3.0, event1.value, 0.001);

        assertEquals(2, event2.ask);
        assertEquals(4, event2.bid);
        assertEquals(6.0, event2.value, 0.001);

        assertEquals(3, event3.ask);
        assertEquals(6, event3.bid);
        assertEquals(9.0, event3.value, 0.001);
    }

    @Test
    public void replayEventsFromAllFiles() throws Exception {

        Event event = mock(Event.class);
        JournalReadState<Event> readState = mock(JournalReadState.class);

        expect(readState.hasRemaining()).andReturn(true).once();
        expect(readState.hasRemaining()).andReturn(true).once();
        expect(readState.hasRemaining()).andReturn(false).once();

        expect(readState.getNextEvent()).andReturn(event).times(2);

        ReplayEventHandler eventHandler = mock(ReplayEventHandler.class);
        eventHandler.onEvent(event, 0L, false);
        expectLastCall().times(2);

        JournalReader<Event> eventJournalReader = new JournalReader<>(readState);
        eventJournalReader.setHandler(eventHandler);

        replay(readState, eventHandler);

        eventJournalReader.replay();

        verify(readState, eventHandler);
    }
}

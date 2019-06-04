package disruptor.event.journal;

import disruptor.event.journal.implementations.Event;
import disruptor.event.journal.persist.channel.FileChannelPersister;
import junit.framework.AssertionFailedError;
import org.junit.Before;
import org.junit.Test;

import java.nio.ByteBuffer;

import static org.easymock.EasyMock.*;
import static org.junit.Assert.assertEquals;

public class JournalTest {

    private FileChannelPersister persister;
    private JournalBufferWriter<Event> bufferWriter;

    private Event event;
    private ByteBuffer byteBuffer;

    @Before
    public void setUp() throws Exception {
        persister = mock(FileChannelPersister.class);
        bufferWriter = mock(JournalBufferWriter.class);
        byteBuffer = mock(ByteBuffer.class);

        event = new Event();
    }

    @Test
    public void dynamicMountTest() {
        Journal<Event> journal = new Journal(persister, bufferWriter, byteBuffer);

        bufferWriter.writeToBuffer(event, byteBuffer);
        expectLastCall().once();

        replay(bufferWriter);

        journal.mount();
        journal.write(event);

        verify(bufferWriter);
    }

    @Test
    public void dynamicDismountTest() {
        Journal<Event> journal = new Journal(persister, bufferWriter, byteBuffer);

        bufferWriter.writeToBuffer(event, byteBuffer);
        expectLastCall().andThrow(new AssertionFailedError()).anyTimes();

        replay(bufferWriter);

        journal.dismount();
        journal.write(event);

        verify(bufferWriter);
    }

    @Test
    public void journalingFlowTest() {
        Journal<Event> journal = new Journal(persister, bufferWriter, byteBuffer);

        bufferWriter.writeToBuffer(event, byteBuffer);
        expectLastCall().once();

        persister.handleBuffer(byteBuffer);
        expectLastCall().once();

        replay(bufferWriter);

        journal.write(event);

        verify(bufferWriter);
    }
}

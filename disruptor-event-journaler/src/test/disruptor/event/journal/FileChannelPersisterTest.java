package disruptor.event.journal;

import disruptor.event.journal.persist.channel.FileChannelPersister;
import disruptor.event.journal.persist.channel.FileChannelProvider;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

import static org.easymock.EasyMock.*;

public class FileChannelPersisterTest {

    private FileChannelProvider provider;
    private FileChannel fileChannel;
    private ByteBuffer buffer;

    @Before
    public void setUp() throws Exception {
        provider = mock(FileChannelProvider.class);
        fileChannel = createMockBuilder(FileChannel.class).createMock();

        buffer = ByteBuffer.allocateDirect(1024);
        buffer.flip();
    }

    @Test
    public void writeTest() throws IOException {

        expect(provider.provide()).andReturn(fileChannel);
        expect(fileChannel.size()).andReturn(1024L);
        expect(fileChannel.write(buffer)).andReturn(1024);

        replay(provider, fileChannel);

        FileChannelPersister persister = new FileChannelPersister(provider);
        persister.setFileSize(1024L);
        persister.handleBuffer(buffer);

        verify(provider, fileChannel);
    }
}

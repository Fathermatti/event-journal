package disruptor.event.journal;

import disruptor.event.journal.persist.channel.ConcurrentFileChannelProvider;
import disruptor.event.journal.persist.channel.FileChannelProvider;
import org.junit.Test;

import java.nio.channels.FileChannel;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertNotNull;
import static org.easymock.EasyMock.*;

public class ConcurrentFileChannelProviderTest {

    @Test
    public void providerInitializationTest() {

        FileChannel channel = mock(FileChannel.class);
        FileChannelProvider fileChannelProvider = mock(FileChannelProvider.class);

        expect(fileChannelProvider.provide()).andReturn(channel).times(4);

        replay(fileChannelProvider);

        ConcurrentFileChannelProvider concurrentFileChannelProvider = new ConcurrentFileChannelProvider(4, fileChannelProvider);

        verify(fileChannelProvider);
    }

    @Test
    public void providerAndThreadStartTest() {

        FileChannel channel = mock(FileChannel.class);
        FileChannelProvider fileChannelProvider = mock(FileChannelProvider.class);

        expect(fileChannelProvider.provide()).andReturn(channel).times(4, 6);

        replay(fileChannelProvider);

        ConcurrentFileChannelProvider concurrentFileChannelProvider = new ConcurrentFileChannelProvider(4, fileChannelProvider);

        Thread thread = new Thread(concurrentFileChannelProvider);
        thread.start();

        try {
            concurrentFileChannelProvider.terminate();

            thread.interrupt();
            thread.join();

        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        verify(fileChannelProvider);
    }

    @Test
    public void continuousProvidingOfFileChannelsTest() throws InterruptedException {

        FileChannel channel = mock(FileChannel.class);
        FileChannelProvider fileChannelProvider = mock(FileChannelProvider.class);

        expect(fileChannelProvider.provide()).andReturn(channel).times(5, 9);

        replay(fileChannelProvider);

        ConcurrentFileChannelProvider concurrentFileChannelProvider = new ConcurrentFileChannelProvider(4, fileChannelProvider);
        Thread thread = new Thread(concurrentFileChannelProvider);
        thread.start();

        assertEquals(4, concurrentFileChannelProvider.channelQueue.size());
        assertEquals(0, concurrentFileChannelProvider.channelQueue.remainingCapacity());

        FileChannel channel1 = concurrentFileChannelProvider.provide();
        FileChannel channel2 = concurrentFileChannelProvider.provide();
        FileChannel channel3 = concurrentFileChannelProvider.provide();
        FileChannel channel4 = concurrentFileChannelProvider.provide();

        FileChannel refilledChannel = concurrentFileChannelProvider.channelQueue.take();

        assertNotNull(channel1);
        assertNotNull(channel2);
        assertNotNull(channel3);
        assertNotNull(channel4);
        assertNotNull(refilledChannel);

        try {
            concurrentFileChannelProvider.terminate();

            thread.interrupt();
            thread.join();

        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}

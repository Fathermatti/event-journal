package disruptor.event.journal.persist.channel;

import disruptor.event.journal.JournalFileManager;
import disruptor.event.journal.persist.OutputMethodProvider;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.nio.channels.FileChannel;
import java.util.concurrent.ArrayBlockingQueue;

public class ConcurrentFileChannelProvider implements OutputMethodProvider<FileChannel>, Runnable {

    private volatile boolean keepAlive = true;
    private FileChannelProvider provider;

    public ArrayBlockingQueue<FileChannel> channelQueue;

    public ConcurrentFileChannelProvider(int queueSize, FileChannelProvider provider) {
        this.provider = provider;

        channelQueue = new ArrayBlockingQueue<>(queueSize);

        for (int i = 0; i < queueSize; i++) {
            FileChannel channel = provider.provide();
            channelQueue.offer(channel);
        }
    }

    @Override
    public FileChannel provide() {
        return channelQueue.poll();
    }

    public void terminate() { keepAlive = false; }

    @Override
    public void run() {

        while (keepAlive) {

                FileChannel channel = provider.provide();

                try {
                    channelQueue.put(channel);

                } catch (InterruptedException e) {
                    System.out.println("Provider queue thread interrupted.");
            }
        }
    }
}

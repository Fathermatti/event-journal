package disruptor.event.journal.persist.channel;

import disruptor.event.journal.persist.JournalPersister;
import disruptor.event.journal.persist.OutputMethodProvider;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

public class NonClosingFileChannelPersister implements JournalPersister {

    private FileChannel fileChannel;

    private long journalFileSize;
    private OutputMethodProvider<FileChannel> provider;

    public NonClosingFileChannelPersister(OutputMethodProvider<FileChannel> provider) {
        this.provider = provider;
        this.fileChannel = provider.provide();
    }

    @Override
    public void setFileSize(long journalFileSize) {
        this.journalFileSize = journalFileSize;
    }

    @Override
    public void handleBuffer(ByteBuffer byteBuffer) {

        try {
            if (fileChannel.size() > journalFileSize) {
                replaceFileChannel();
            }

            byteBuffer.flip();
            fileChannel.write(byteBuffer);
            byteBuffer.clear();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void replaceFileChannel() {
        fileChannel = provider.provide();
    }

    @Override
    public void flush(ByteBuffer byteBuffer) {
        try {
            byteBuffer.flip();
            fileChannel.write(byteBuffer);
            byteBuffer.clear();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void close() throws Exception {
        fileChannel.close();
    }
}

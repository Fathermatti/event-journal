package disruptor.event.journal.persist.mapped;

import disruptor.event.journal.JournalFileManager;
import disruptor.event.journal.persist.JournalPersister;

import java.nio.ByteBuffer;
import java.nio.MappedByteBuffer;

public class MappedBufferPersister implements JournalPersister {

    private final JournalFileManager journalFileManager;
    MappedByteBuffer mappedByteBuffer;
    private MappedByteBufferProvider mappedByteBufferProvider;
    private long journalFileSize;

    public MappedBufferPersister(JournalFileManager journalFileManager, MappedByteBufferProvider mappedByteBufferProvider) {
        this.journalFileManager = journalFileManager;
        this.mappedByteBufferProvider = mappedByteBufferProvider;

        roll();
    }

    public void roll() {
        mappedByteBuffer = mappedByteBufferProvider.provide();
    }

    @Override
    public void setFileSize(long journalFileSize) {
        this.journalFileSize = journalFileSize;
    }

    @Override
    public void handleBuffer(ByteBuffer byteBuffer) {
        if (mappedByteBuffer.position() > journalFileSize) {
            roll();
        }
    }

    @Override
    public void flush(ByteBuffer byteBuffer) {
        mappedByteBuffer.force();

    }

    @Override
    public void close() {
        mappedByteBuffer = null;
    }
}

package disruptor.event.journal;

import disruptor.event.journal.persist.channel.FileChannelPersister;
import disruptor.event.journal.persist.channel.FileChannelProvider;
import disruptor.event.journal.persist.JournalPersister;

import java.nio.ByteBuffer;
import java.util.function.Supplier;

public class JournalBuilder<T> {

    public final long DEFAULT_JOURNAL_FILE_SIZE = 50000000L; // 50 mega byte
    public final int DEFAULT_BUFFER_SIZE = 8192; // 8 kilo byte

    private long journalFileSize;
    private int bufferSize;

    private JournalPersister persister;
    private JournalBufferWriter bufferWriter;

    private JournalBuilder () {
        journalFileSize = DEFAULT_JOURNAL_FILE_SIZE;
        bufferSize = DEFAULT_BUFFER_SIZE;
    }

    private JournalPersister getDefaultPersister() {
        JournalDirectoryCreater disc = new JournalDirectoryCreater();

        return new FileChannelPersister(
                new FileChannelProvider(new JournalFileManager(disc.createJournalDirectory())));
    }

    public JournalBuilder<T> withPersister(JournalPersister persister) {
        this.persister = persister;
        return this;
    }

    public JournalBuilder<T> withBufferWriter(JournalBufferWriter bufferWriter) {
        this.bufferWriter = bufferWriter;
        return this;
    }

    public JournalBuilder<T> withBufferWriter(Supplier<JournalBufferWriter> constructor) {
        this.bufferWriter = constructor.get();
        return this;
    }

    public JournalBuilder<T> withBufferSize(int bufferSize) {
        this.bufferSize = bufferSize;
        return this;
    }

    public JournalBuilder<T> withJournalFileSize(long journalFileSize) {
        this.journalFileSize = journalFileSize;
        return this;
    }

    public static <T> JournalBuilder<T> create() {
        return new JournalBuilder<T>();
    }

    public Journal<T> build() {

        if (bufferWriter == null) {
            throw new IllegalArgumentException("A buffer writer is required.");
        }

        if (persister == null) {
            persister = getDefaultPersister();
        }

        persister.setFileSize(journalFileSize);
        ByteBuffer byteBuffer = ByteBuffer.allocateDirect(bufferSize);

        return new Journal<T>(persister, bufferWriter, byteBuffer);
    }
}

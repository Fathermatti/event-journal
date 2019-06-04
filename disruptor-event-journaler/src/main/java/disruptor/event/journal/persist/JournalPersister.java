package disruptor.event.journal.persist;

import java.nio.ByteBuffer;

public interface JournalPersister extends AutoCloseable {
    public void setFileSize(long journalFileSize);
    public void handleBuffer(ByteBuffer byteBuffer);
    public void flush(ByteBuffer byteBuffer);
}
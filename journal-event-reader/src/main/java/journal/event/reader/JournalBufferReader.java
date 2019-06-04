package journal.event.reader;

import java.nio.ByteBuffer;

public interface JournalBufferReader<T> {

    public T getEvent(ByteBuffer byteBuffer);
}

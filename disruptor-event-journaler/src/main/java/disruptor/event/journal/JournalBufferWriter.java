package disruptor.event.journal;

import java.nio.ByteBuffer;

public interface JournalBufferWriter<T> {

    public void writeToBuffer(T event, ByteBuffer byteBuffer);
}
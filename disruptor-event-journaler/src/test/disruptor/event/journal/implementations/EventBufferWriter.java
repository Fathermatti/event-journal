package disruptor.event.journal.implementations;

import disruptor.event.journal.JournalBufferWriter;

import java.nio.ByteBuffer;

public class EventBufferWriter implements JournalBufferWriter<Event> {

    @Override
    public void writeToBuffer(Event event, ByteBuffer byteBuffer) {

        byteBuffer.putLong(event.simpleValue);
        byteBuffer.putLong(event.anotherValue);
        byteBuffer.putLong(event.andAThirdValue);
    }
}

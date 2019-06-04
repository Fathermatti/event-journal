package journal.event.reader;

import java.nio.ByteBuffer;

public class EventBufferReader implements JournalBufferReader<Event> {

    @Override
    public Event getEvent(ByteBuffer byteBuffer) {
        Event event = new Event();

        event.ask = byteBuffer.getLong();
        event.bid = byteBuffer.getInt();
        event.value = byteBuffer.getDouble();

        return event;
    }
}

package journal.benchmarks;

import java.nio.ByteBuffer;

import disruptor.event.journal.JournalBufferWriter;

public class EventBufferWriter implements JournalBufferWriter<Event> {

    @Override
    public void writeToBuffer(Event event, ByteBuffer buffer) {

	buffer.putLong(event.val1);
	buffer.putLong(event.val2);
	buffer.putLong(event.val3);

//	buffer.putInt(event.val4);
//	buffer.putInt(event.val5);
//	buffer.putInt(event.val6);
//	
//	buffer.putDouble(event.val7);
//	buffer.putDouble(event.val8);
//	buffer.putDouble(event.val9);
	
    }

}

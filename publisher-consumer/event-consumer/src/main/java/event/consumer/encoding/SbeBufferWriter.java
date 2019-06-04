package event.consumer.encoding;

import disruptor.event.journal.JournalBufferWriter;
import disruptor.event.journal.encoding.PriceEventEncoder;
import event.consumer.domain.InputEvent;
import org.agrona.concurrent.UnsafeBuffer;

import java.nio.ByteBuffer;

public class SbeBufferWriter implements JournalBufferWriter<InputEvent> {

    private static final UnsafeBuffer unsafeBuffer = new UnsafeBuffer();
    private static final PriceEventEncoder PRICE_EVENT_ENCODER = new PriceEventEncoder();

    @Override
    public void writeToBuffer(InputEvent inputEvent, ByteBuffer byteBuffer) {
        int position = byteBuffer.position();
        unsafeBuffer.wrap(byteBuffer);

        PRICE_EVENT_ENCODER.wrap(unsafeBuffer,  position);
        PRICE_EVENT_ENCODER.ask(123L);
        PRICE_EVENT_ENCODER.bid(456L);
        PRICE_EVENT_ENCODER.timeStamp(789L);

        byteBuffer.position(position + PRICE_EVENT_ENCODER.encodedLength());
    }
}

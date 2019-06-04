package journal.event.reader.samples;

import journal.event.reader.JournalBufferReader;
import org.agrona.concurrent.UnsafeBuffer;

import java.nio.ByteBuffer;

public class InputEventBufferReader implements JournalBufferReader<InputEvent> {

    public SbeDecoder decoder = new SbeDecoder();
    public int eventSize = decoder.sbeBlockLength();
    public UnsafeBuffer unsafeBuffer = new UnsafeBuffer();

    @Override
    public InputEvent getEvent(ByteBuffer byteBuffer) {

        int position = byteBuffer.position();

        unsafeBuffer.wrap(byteBuffer);
        decoder.wrap(unsafeBuffer, position, eventSize, 1);

        byteBuffer.position(position + eventSize);

        return new InputEvent(decoder.bid(), decoder.ask());
    }
}

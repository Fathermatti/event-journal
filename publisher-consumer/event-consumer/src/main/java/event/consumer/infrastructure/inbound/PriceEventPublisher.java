package event.consumer.infrastructure.inbound;

import disruptor.event.journal.encoding.PriceEventDecoder;
import event.consumer.domain.InputEvent;
import org.agrona.DirectBuffer;

import com.google.inject.Inject;
import com.lmax.disruptor.RingBuffer;


public class PriceEventPublisher
{
	private RingBuffer<InputEvent> ringBuffer;

	@Inject
	public PriceEventPublisher(RingBuffer<InputEvent> ringBuffer) 
	{
		this.ringBuffer = ringBuffer;
	}
	
    private static final PriceEventDecoder PRICE_EVENT_DECODER = new PriceEventDecoder();

	public void decodeAndPublishEvent(final DirectBuffer directBuffer, final int bufferOffset, final int blockLength, final int version)
	{
		PRICE_EVENT_DECODER.wrap(directBuffer, bufferOffset, blockLength, version);
		
		ringBuffer.publishEvent((event, sequence, buffer) -> { 
			event.type = InputEvent.Type.SKEW;
			event.ask = PRICE_EVENT_DECODER.ask();
			event.bid = PRICE_EVENT_DECODER.bid();
			event.askSkew = PRICE_EVENT_DECODER.timeStamp();
	});
	}
}

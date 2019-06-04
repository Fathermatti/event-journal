package event.consumer.infrastructure.inbound;

import com.google.inject.Inject;

import disruptor.event.journal.encoding.PriceEventDecoder;
import io.aeron.archive.codecs.MessageHeaderDecoder;
import io.aeron.logbuffer.FragmentHandler;

public class PriceEventFragmentHandler
{
	private PriceEventPublisher publisher;

	@Inject
	public PriceEventFragmentHandler(PriceEventPublisher publisher) 
	{
		this.publisher = publisher;
	}
	
	private static final MessageHeaderDecoder MESSAGE_HEADER_DECODER = new MessageHeaderDecoder();
	
    public FragmentHandler GetHandler()
    {
        return (buffer, offset, length, header) ->
        {
            MESSAGE_HEADER_DECODER.wrap(buffer, offset);

    		final int actingBlockLength = MESSAGE_HEADER_DECODER.blockLength();
    		final int actingVersion = MESSAGE_HEADER_DECODER.version();
    		
            if (!validateTemplateId()) {
            	throw new IllegalStateException("Template ids do not match.");
            }

            offset += MESSAGE_HEADER_DECODER.encodedLength();
            
        	publisher.decodeAndPublishEvent(buffer, offset, actingBlockLength, actingVersion);
        };
    }
    
    private static boolean validateTemplateId() 
    {
        final int templateId = MESSAGE_HEADER_DECODER.templateId();
        if (templateId == PriceEventDecoder.TEMPLATE_ID)
        {
            return true;
        }
        else 
        {
        	return false;
        }
    }
}

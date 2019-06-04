package event.consumer.infrastructure.inbound;

import java.util.concurrent.atomic.AtomicBoolean;

import org.agrona.CloseHelper;
import org.agrona.concurrent.BusySpinIdleStrategy;
import org.agrona.concurrent.SigInt;

import com.google.inject.Inject;

import io.aeron.Aeron;
import io.aeron.Aeron.Context;
import io.aeron.Subscription;
import io.aeron.driver.MediaDriver;
import io.aeron.samples.SampleConfiguration;

public class AeronPriceEventSubscriber
{
	private Context aeronContext;
	private PriceEventSubscriberLoop priceEventSubscriberLoop;
	private MediaDriver driver;

	@Inject
	public AeronPriceEventSubscriber(Aeron.Context aeronContext, PriceEventSubscriberLoop priceEventSubscriberLoop, MediaDriver driver) 
	{
		this.aeronContext = aeronContext;
		this.priceEventSubscriberLoop = priceEventSubscriberLoop;
		this.driver = driver;
	}
	
    private static final int STREAM_ID = SampleConfiguration.STREAM_ID;
    private static final String CHANNEL = SampleConfiguration.CHANNEL;
    private static final int FRAGMENT_COUNT_LIMIT = SampleConfiguration.FRAGMENT_COUNT_LIMIT;
	
    private static final boolean EMBEDDED_MEDIA_DRIVER = true;

    public void Subscribe()
    {	
        System.out.println("Subscribing to " + CHANNEL + " on stream Id " + STREAM_ID);

        if (EMBEDDED_MEDIA_DRIVER)
        {
        	aeronContext.aeronDirectoryName(driver.aeronDirectoryName());
        }

        final AtomicBoolean running = new AtomicBoolean(true);
        SigInt.register(() -> running.set(false));
      
        try (Aeron aeron = Aeron.connect(aeronContext);
            Subscription subscription = aeron.addSubscription(CHANNEL, STREAM_ID))
        {
        	priceEventSubscriberLoop.getLoop(FRAGMENT_COUNT_LIMIT, running, new BusySpinIdleStrategy())
            		.accept(subscription);

            System.out.println("Shutting down...");
        }

        CloseHelper.quietClose(driver);
    }

}
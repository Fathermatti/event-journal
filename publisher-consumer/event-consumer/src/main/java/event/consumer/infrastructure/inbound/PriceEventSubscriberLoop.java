package event.consumer.infrastructure.inbound;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Consumer;

import org.agrona.LangUtil;
import org.agrona.concurrent.IdleStrategy;

import com.google.inject.Inject;

import io.aeron.Subscription;
import io.aeron.logbuffer.FragmentHandler;

public class PriceEventSubscriberLoop
{
	private final FragmentHandler fragmentHandler;

	@Inject
	public PriceEventSubscriberLoop(PriceEventFragmentHandler fragmentHandler)
	{
		this.fragmentHandler = fragmentHandler.GetHandler();

	}

	public Consumer<Subscription> getLoop(final int limit, final AtomicBoolean running, final IdleStrategy idleStrategy)
	{
		return (subscription) -> {
			try
			{
				while (running.get())
				{
					final int fragmentsRead = subscription.poll(fragmentHandler, limit);
					idleStrategy.idle(fragmentsRead);
				}
			} catch (final Exception ex)
			{
				LangUtil.rethrowUnchecked(ex);
			}
		};
	}

}

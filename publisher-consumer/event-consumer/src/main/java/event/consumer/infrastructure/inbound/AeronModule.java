package event.consumer.infrastructure.inbound;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;

import io.aeron.Aeron;
import io.aeron.driver.MediaDriver;
import io.aeron.samples.SamplesUtil;

public class AeronModule extends AbstractModule
{
	private static final boolean EMBEDDED_MEDIA_DRIVER = true;

	@Override
	protected void configure()
	{
	}

	@Provides
	Aeron.Context provideAeronContext()
	{
		Aeron.Context context = new Aeron.Context().availableImageHandler(SamplesUtil::printAvailableImage)
				.unavailableImageHandler(SamplesUtil::printUnavailableImage);

		return context;
	}

	@Provides
	MediaDriver provideAeronMediaDriver()
	{
		return EMBEDDED_MEDIA_DRIVER ? MediaDriver.launchEmbedded() : null;
	}
}

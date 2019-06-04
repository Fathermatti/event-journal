package event.consumer.application;

import com.google.inject.Guice;
import com.google.inject.Injector;

import event.consumer.infrastructure.DisruptorModule;
import event.consumer.infrastructure.DisruptorRemaningCapacityObserver;
import event.consumer.infrastructure.JournalModule;
import event.consumer.infrastructure.inbound.AeronModule;
import event.consumer.infrastructure.inbound.AeronPriceEventSubscriber;

public class Application
{
	public static void main(String[] args)
	{
		Injector injector = Guice.createInjector(new DisruptorModule(), new AeronModule(), new JournalModule());
		AeronPriceEventSubscriber subscriber = injector.getInstance(AeronPriceEventSubscriber.class);

		DisruptorRemaningCapacityObserver logger = injector.getInstance(DisruptorRemaningCapacityObserver.class);
		Thread loggerThread = new Thread(logger.GetRunnable());
		loggerThread.start();

		subscriber.Subscribe();
	}
}

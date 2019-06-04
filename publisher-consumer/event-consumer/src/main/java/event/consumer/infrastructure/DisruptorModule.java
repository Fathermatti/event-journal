package event.consumer.infrastructure;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import com.lmax.disruptor.RingBuffer;
import com.lmax.disruptor.YieldingWaitStrategy;
import com.lmax.disruptor.dsl.Disruptor;
import com.lmax.disruptor.dsl.ProducerType;
import com.lmax.disruptor.util.DaemonThreadFactory;

import event.consumer.domain.InputEvent;
import event.consumer.domain.InputEventHandler;

public class DisruptorModule extends AbstractModule
{  
	@Override 
	protected void configure() 
	{
		bind(DisruptorRemaningCapacityObserver.class);
	}
	
	@Provides
	@Singleton
	RingBuffer<InputEvent> provideInputEventRingBuffer(InputEventHandler inputEventHandler)
	{
		int bufferSize = 1024;

		Disruptor<InputEvent> disruptor = new Disruptor<>(InputEvent::new, bufferSize, DaemonThreadFactory.INSTANCE,
				ProducerType.SINGLE, new YieldingWaitStrategy());

		disruptor.handleEventsWith(inputEventHandler);

		disruptor.start();

		return disruptor.getRingBuffer();
	}
	
}



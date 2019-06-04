package event.consumer.infrastructure;

import com.google.inject.Inject;
import com.lmax.disruptor.RingBuffer;

import event.consumer.domain.InputEvent;

public class DisruptorRemaningCapacityObserver
{
	private RingBuffer<InputEvent> ringBuffer;

	@Inject
	public DisruptorRemaningCapacityObserver(RingBuffer<InputEvent> ringBuffer)
	{
		this.ringBuffer = ringBuffer;
	}
	
	public Runnable GetRunnable()
	{
		return new Runnable()
		{
			@Override
			public void run()
			{
				System.out.println("Polling on remaining capacity in RingBuffer:");
				
				while (true)
				{
					if (ringBuffer.remainingCapacity() < 100) 
					{
						System.out.println("Capacity now down to: " + ringBuffer.remainingCapacity());	
					}
					
					try
					{
						Thread.sleep(100);
					} 
					catch (InterruptedException e)
					{
						e.printStackTrace();
					}
				}
			}
		};
	}
}

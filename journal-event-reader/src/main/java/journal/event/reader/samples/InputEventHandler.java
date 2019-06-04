package journal.event.reader.samples;

import com.lmax.disruptor.EventHandler;

public class InputEventHandler implements EventHandler<InputEvent> {

    @Override
    public void onEvent(InputEvent event, long sequence, boolean endOfBatch) throws Exception {
        System.out.println("Ask: " + event.ask + " Bid: " + event.bid);

        return;
    }
}

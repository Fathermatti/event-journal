package journal.event.reader;

public class ReplayEventHandler implements com.lmax.disruptor.EventHandler<Event> {
    @Override
    public void onEvent(Event event, long sequence, boolean endOfBatch) throws Exception {

    }
}

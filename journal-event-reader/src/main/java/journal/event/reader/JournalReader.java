package journal.event.reader;

import com.lmax.disruptor.EventHandler;

public class JournalReader<T> {

    private JournalReadState<T> journalReadState;
    private EventHandler handler;

    public JournalReader(JournalReadState<T> journalReadState) {
        this.journalReadState = journalReadState;
    }

    public void setHandler(EventHandler handler) {
        this.handler = handler;
    }

    public boolean hasRemainingEvent() {
        return journalReadState.hasRemaining();
    }

    public T getNextEvent() {
        if (journalReadState.hasRemaining()) {
            return journalReadState.getNextEvent();
        }
        else {
            return null;
        }
    }

    public void replay() {

        if (handler == null) {
            throw new IllegalStateException("Set a handler in order to replay events.");
        }

        while(journalReadState.hasRemaining()) {
            T event = journalReadState.getNextEvent();

            try {
                handler.onEvent(event, 0L, false);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}

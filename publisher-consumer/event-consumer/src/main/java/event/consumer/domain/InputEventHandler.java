package event.consumer.domain;

import com.google.inject.Inject;
import com.lmax.disruptor.EventHandler;

import disruptor.event.journal.Journal;

public class InputEventHandler implements EventHandler<InputEvent> {
    private Journal journal;

    @Inject
    public InputEventHandler(Journal journal) {
        this.journal = journal;
    }

    @Override
    public void onEvent(InputEvent event, long sequence, boolean endOfBatch) throws Exception {
        double value = 2;

        for (int i = 0; i < 100; i++) {
            value = event.ask * i + value;
        }

        journal.write(event);
    }
}
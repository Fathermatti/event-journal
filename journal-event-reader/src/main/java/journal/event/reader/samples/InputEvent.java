package journal.event.reader.samples;

public class InputEvent {

    public final long ask;
    public final long bid;

    public InputEvent(long ask, long bid) {
        this.ask = ask;
        this.bid = bid;
    }
}

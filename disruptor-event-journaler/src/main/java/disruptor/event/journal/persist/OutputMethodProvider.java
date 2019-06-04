package disruptor.event.journal.persist;

public interface OutputMethodProvider<T> {
    public T provide();
}

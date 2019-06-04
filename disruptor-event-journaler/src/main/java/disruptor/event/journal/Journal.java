package disruptor.event.journal;

import disruptor.event.journal.persist.JournalPersister;

import java.nio.ByteBuffer;

public class Journal<T>
{
	private boolean isMounted;

	private JournalPersister<T> journalPersister;
	private JournalBufferWriter journalBufferWriter;
	private ByteBuffer byteBuffer;

	public Journal(
			JournalPersister<T> journalPersister,
			JournalBufferWriter journalBufferWriter,
			ByteBuffer byteBuffer)
	{
		this.journalPersister = journalPersister;
		this.journalBufferWriter = journalBufferWriter;
		this.byteBuffer = byteBuffer;

		this.isMounted = true;
	}

	public void write(T event)
	{
		if (!isMounted) {
			return;
		}

		journalBufferWriter.writeToBuffer(event, byteBuffer);
		journalPersister.handleBuffer(byteBuffer);
	}

	public void mount() {
		isMounted = true;
	}

	public void dismount() {
		isMounted = false;
	}

	public void shutdown() {

		dismount();

		try {
			journalPersister.flush(byteBuffer);
			journalPersister.close();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}

package journal.event.reader;

import java.nio.MappedByteBuffer;
import java.nio.file.Path;

public class JournalReadState<T> {

    private JournalBufferReader<T> bufferReader;
    private final JournalReaderMemoryMapper memoryMapper;
    private JournalReaderFiles readerFiles;

    private MappedByteBuffer mappedJournalBuffer;

    public JournalReadState(JournalBufferReader<T> bufferReader, JournalReaderMemoryMapper memoryMapper, JournalReaderFiles readerFiles) {
        this.bufferReader = bufferReader;
        this.memoryMapper = memoryMapper;
        this.readerFiles = readerFiles;

        setNextJournalBuffer();
    }

    private void setNextJournalBuffer() {
        mappedJournalBuffer = memoryMapper.getNextMappedBuffer();
    }

    public boolean hasRemaining() {
        if (mappedJournalBuffer.position() < mappedJournalBuffer.limit()) {
            return true;
        }
        else if (memoryMapper.hasRemainingBytes()) {
            return true;
        }
        else if (readerFiles.hasRemainingFiles()) {
            return true;
        }
        else {
            return false;
        }
    }

    public T getNextEvent() {
        if (mappedJournalBuffer.position() == mappedJournalBuffer.limit())
        {
            setNextJournalBuffer();
        }

        return bufferReader.getEvent(mappedJournalBuffer);
    }
}

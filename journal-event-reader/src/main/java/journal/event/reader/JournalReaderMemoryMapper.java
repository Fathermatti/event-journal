package journal.event.reader;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;

public class JournalReaderMemoryMapper {

    private JournalReaderFiles journalReaderFiles;

    private RandomAccessFile file;
    private long remainingBytesInFile;

    public JournalReaderMemoryMapper(JournalReaderFiles journalReaderFiles) {
        this.journalReaderFiles = journalReaderFiles;
        setNextFile();
    }

    private void setNextFile() {
        file = journalReaderFiles.getNextFile();
        remainingBytesInFile = journalReaderFiles.getFileLength(file);
    }

    public boolean hasRemainingBytes() {
        return remainingBytesInFile != 0;
    }

    public MappedByteBuffer getNextMappedBuffer() {

        if (hasRemainingBytes()) {
            return createNextMappedBuffer();

        } else {
            setNextFile();
            return createNextMappedBuffer();
        }
    }

    private MappedByteBuffer createNextMappedBuffer() {

        int mapSize = (int) Math.min(remainingBytesInFile, Integer.MAX_VALUE);
        remainingBytesInFile -= mapSize;

        return createMappedBuffer(file, mapSize);
    }

    private MappedByteBuffer createMappedBuffer(RandomAccessFile file, long mapSize) {

        MappedByteBuffer buffer = null;

        try {
            buffer = file.getChannel()
                    .map(FileChannel.MapMode.READ_WRITE, 0, mapSize);
            buffer.clear();

        } catch (IOException e) {
            e.printStackTrace();

        }

        return buffer;
    }
}

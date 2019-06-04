package journal.event.reader;

import java.nio.file.Path;
import java.util.List;

public class JournalReaderFactory<T> {

    private JournalReaderFactory() {
    }

    public static <T> JournalReader<T> createJournalReader(JournalBufferReader<T> bufferReader) {

        List<Path> paths = new JournalFileDiscoverer()
                .getJournalFilePaths();

        return createJournalReader(bufferReader, paths);
    }

    public static <T> JournalReader<T> createJournalReader(JournalBufferReader<T> bufferReader, String journalDirectory) {

        List<Path> paths = new JournalFileDiscoverer()
                .getJournalFilePaths(journalDirectory);

        return createJournalReader(bufferReader, paths);
    }

    public static <T> JournalReader<T> createJournalReader(JournalBufferReader<T> bufferReader, List<Path> filePaths) {

        JournalReaderFiles files = new JournalReaderFiles(filePaths);
        JournalReaderMemoryMapper mapper = new JournalReaderMemoryMapper(files);
        JournalReadState<T> state = new JournalReadState(bufferReader, mapper, files);

        return new JournalReader(state);
    }
}

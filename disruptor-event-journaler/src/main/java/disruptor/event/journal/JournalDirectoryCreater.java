package disruptor.event.journal;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class JournalDirectoryCreater {

    private final String DIRECTORY_NAME_PREFIX = "journal-files";

    public Path createJournalDirectory() {
        return createJournalDirectory(DIRECTORY_NAME_PREFIX);
    }

    public Path createJournalDirectory(String directoryName) {
        Path createdDirectory = null;

        try {
            Path directoryPath = getDirectoryPath(directoryName);
            createdDirectory = Files.createDirectory(directoryPath);

        } catch (IOException e) {
            e.printStackTrace();
        }

        return createdDirectory;
    }

    public Path getDirectoryPath(String directoryPrefix) {

        int directoryIndex = 0;
        Path path;

        do {
            path = Paths.get(directoryPrefix + "-" + directoryIndex);
            directoryIndex++;
        } while (Files.exists(path));

        return path;
    }
}

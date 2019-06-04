package disruptor.event.journal;

import java.io.IOException;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class JournalFileManager {

    public Path journalDirectory;
    public int fileIndex = 0;

    public JournalFileManager(Path journalDirectory) {
        this.journalDirectory = journalDirectory;
    }

    public Path getFilePath() {

        Path file = null;

        try {
            file = Files.createFile(journalDirectory.resolve("journal-" + fileIndex + ".jnl"));
            fileIndex++;

        } catch (IOException e) {
            e.printStackTrace();
        }

        return file;
    }
}


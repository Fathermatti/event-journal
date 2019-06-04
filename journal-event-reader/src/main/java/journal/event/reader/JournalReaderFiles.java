package journal.event.reader;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.file.Path;
import java.util.List;

public class JournalReaderFiles {

    private List<Path> journalFilePaths;
    private int filePathIndex = 0;

    public JournalReaderFiles(List<Path> journalFilePaths) {
        this.journalFilePaths = journalFilePaths;
    }

    public boolean hasRemainingFiles() {
        return filePathIndex < journalFilePaths.size();
    }

    public RandomAccessFile getNextFile() {
        Path filePath = journalFilePaths.get(filePathIndex);
        filePathIndex++;

        printReplayMessage(filePath);

        return openJournalFile(filePath);
    }

    private RandomAccessFile openJournalFile(Path path) {
        RandomAccessFile file = null;

        try {
            file = new RandomAccessFile(path.toFile(), "rw");

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        return file;
    }

    public long getFileLength(RandomAccessFile file) {
        long fileLength = 0;
        try {
            fileLength = file.length();

        } catch (IOException e) {
            e.printStackTrace();
        }

        return fileLength;
    }

    private void printReplayMessage(Path filePath) {
        System.out.println("\nReplaying events from " + filePath + "...\n");
    }
}

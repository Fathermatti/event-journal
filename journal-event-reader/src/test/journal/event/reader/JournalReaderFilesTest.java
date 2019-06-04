package journal.event.reader;

import org.junit.Test;

import java.io.RandomAccessFile;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;

import static org.junit.Assert.assertEquals;

public class JournalReaderFilesTest {

    @Test
    public void hasRemainingFilesTest() {

        Path path1 = Paths.get("test-1");
        Path path2 = Paths.get("test-2");

        ArrayList<Path> paths = new ArrayList();
        paths.add(path1);
        paths.add(path2);

        JournalReaderFiles journalReaderFiles = new JournalReaderFiles(paths);

        boolean hasRemaining1 = journalReaderFiles.hasRemainingFiles();
        RandomAccessFile nextFile1 = journalReaderFiles.getNextFile();

        boolean hasRemaining2 = journalReaderFiles.hasRemainingFiles();
        RandomAccessFile nextFile2 = journalReaderFiles.getNextFile();

        boolean hasRemaining3 = journalReaderFiles.hasRemainingFiles();

        assertEquals(true, hasRemaining1);
        assertEquals(true, hasRemaining2);
        assertEquals(false, hasRemaining3);
    }
}


package disruptor.event.journal;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.junit.Assert.*;

public class JournalDirectoryCreaterTest {

    String testDirectoryName = "journal-test-directory";

    String testDirectory0 = "journal-test-directory-0";
    String testDirectory1 = "journal-test-directory-1";
    String testDirectory2 = "journal-test-directory-2";

    @Before
    public void setUp() throws Exception {
        clearAllDirectories();
    }

    @Test
    public void createDirectoryTest() {

        JournalDirectoryCreater creater = new JournalDirectoryCreater();
        Path discoveredDirectory = creater.createJournalDirectory(testDirectoryName);

        assertEquals(discoveredDirectory.getFileName().toString(), testDirectory0);
    }

    @Test
    public void findAvailableJournalDirectoryTest() {

        try {
            Files.createDirectory(Paths.get(testDirectory0));
            Files.createDirectory(Paths.get(testDirectory1));
            Files.createDirectory(Paths.get(testDirectory2));

        } catch (IOException e) {
            e.printStackTrace();
        }

        JournalDirectoryCreater creater = new JournalDirectoryCreater();
        Path discoveredDirectory = creater.createJournalDirectory(testDirectoryName);

        String expectedDirectory = "journal-test-directory-3";

        assertEquals(expectedDirectory, discoveredDirectory.getFileName().toString());
    }

    @After
    public void teardown() throws Exception {
        clearAllDirectories();
    }

    public void clearAllDirectories() {
        try {
            DirectoryUtil.clearAndDeleteDirectory(testDirectory0);
            DirectoryUtil.clearAndDeleteDirectory(testDirectory1);
            DirectoryUtil.clearAndDeleteDirectory(testDirectory2);
            DirectoryUtil.clearAndDeleteDirectory(testDirectoryName + "-3");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

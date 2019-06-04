package disruptor.event.journal;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class JournalFileManagerTest {

    String testDirectoryName = "journal-test-directory";
    String testDirectoryFullName = "journal-test-directory-0";

    private JournalFileManager fileManager;

    @Before
    public void setUp() throws Exception {
        DirectoryUtil.clearAndDeleteDirectory(testDirectoryFullName);

        JournalDirectoryCreater creater = new JournalDirectoryCreater();
        fileManager = new JournalFileManager(creater.createJournalDirectory(testDirectoryFullName));
    }

    @Test
    public void createFilesTest() throws IOException {

        Path filePath = fileManager.getFilePath();
        String fileName = filePath.getFileName().toString();

        assertTrue(fileName.contains("journal-") && fileName.contains(".jnl"));

        Files.deleteIfExists(filePath);
    }

    @Test
    public void fileIndexTest() throws IOException, InterruptedException {

        Path file_1 = fileManager.getFilePath();
        Path file_2 = fileManager.getFilePath();
        Path file_3 = fileManager.getFilePath();

        List<Path> createdPaths = new ArrayList<Path>();
        createdPaths.add(file_1);
        createdPaths.add(file_2);
        createdPaths.add(file_3);

        List<Path> sortedPaths = createdPaths.stream()
                .sorted()
                .collect(Collectors.toList());

        assertEquals(sortedPaths.get(0), createdPaths.get(0));
        assertEquals(sortedPaths.get(1), createdPaths.get(1));
        assertEquals(sortedPaths.get(2), createdPaths.get(2));

        Files.deleteIfExists(file_1);
        Files.deleteIfExists(file_2);
        Files.deleteIfExists(file_3);
    }

    @After
    public void tearDown() throws Exception {

        Files.deleteIfExists(Paths.get(testDirectoryFullName));
    }
}

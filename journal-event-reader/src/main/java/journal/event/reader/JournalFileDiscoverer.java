package journal.event.reader;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class JournalFileDiscoverer {

    public static final String JOURNAL_FILE_EXTENSION = ".jnl";

    public List<Path> getJournalFilePaths() {
        return getJournalFilePaths("journal-files");
    }

    public List<Path> getJournalFilePaths(String directory) {

        List<Path> journalFiles = null;

        try (Stream<Path> walk = Files.walk(Paths.get(directory))) {

            journalFiles = walk
                    .filter(Files::isRegularFile)
                    .filter(x -> x.getFileName().toString().contains(JOURNAL_FILE_EXTENSION))
                    .collect(Collectors.toList());

            printFilesFoundMessage(directory, journalFiles);

        } catch (IOException e) {
            e.printStackTrace();
        }

        return journalFiles;
    }

    private void printFilesFoundMessage(String directory, List<Path> journalFiles) {
        System.out.println("The following journal files were found in the directory: \"" + directory + "\" ----");
        journalFiles.forEach(x -> System.out.println("* " + x.toString()));
    }
}

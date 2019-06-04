package disruptor.event.journal;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class DirectoryUtil {

    public static void clearAndDeleteDirectory(String directory) throws IOException {
        clearDirectory(directory);
        Files.deleteIfExists(Paths.get(directory));
    }

    public static void clearDirectory(String directory) {

        if (!Files.exists(Paths.get(directory))) {
            return;
        }

        List<Path> paths = null;
        try (Stream<Path> walk = Files.walk(Paths.get(directory))) {

            paths = walk
                    .filter(Files::isRegularFile)
                    .filter(x -> x.getFileName().toString().contains("jnl"))
                    .collect(Collectors.toList());

        } catch (IOException e) {
            e.printStackTrace();
        }

        paths.forEach(x -> {
            try {
                Files.deleteIfExists(x);

            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }
}

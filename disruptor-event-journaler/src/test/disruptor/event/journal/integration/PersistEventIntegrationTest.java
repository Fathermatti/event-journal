package disruptor.event.journal.integration;

import disruptor.event.journal.*;
import disruptor.event.journal.implementations.Event;
import disruptor.event.journal.implementations.EventBufferWriter;
import disruptor.event.journal.persist.channel.FileChannelPersister;
import disruptor.event.journal.persist.channel.FileChannelProvider;
import disruptor.event.journal.persist.JournalPersister;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.Assert.assertEquals;

public class PersistEventIntegrationTest {

    String directoryNamePrefix = "integration-test-directory";
    String directoryName = "integration-test-directory-0";

    List<Path> paths = null;
    private Event event;

    @Before
    public void setUp() throws Exception {

        DirectoryUtil.clearAndDeleteDirectory(directoryName);

        event = new Event();
        event.simpleValue = 10L;
        event.anotherValue = 20L;
        event.andAThirdValue = 30L;
    }

    @Test
    public void writeEventsToFilesIntegrationTest() throws IOException, InterruptedException {

        JournalDirectoryCreater creater = new JournalDirectoryCreater();
        JournalFileManager fileManager = new JournalFileManager(creater.createJournalDirectory(directoryNamePrefix));
        JournalPersister persister = new FileChannelPersister(new FileChannelProvider(fileManager));

        JournalBuilder<Event> builder = JournalBuilder.<Event>create()
                .withBufferSize(24)
                .withJournalFileSize(48)
                .withPersister(persister)
                .withBufferWriter(EventBufferWriter::new);

        Journal journal = builder.build();

        for (int i = 0; i < 7; i++) {
            Thread.sleep(1);
            journal.write(event);
        }

        try (Stream<Path> walk = Files.walk(Paths.get(directoryName))) {

            paths = walk
                    .filter(Files::isRegularFile)
                    .filter(x -> x.getFileName().toString().contains("jnl"))
                    .collect(Collectors.toList());

        } catch (IOException e) {
            e.printStackTrace();
        }

        assertEquals(3, paths.size());
    }
}

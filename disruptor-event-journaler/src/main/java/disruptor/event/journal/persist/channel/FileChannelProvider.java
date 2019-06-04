package disruptor.event.journal.persist.channel;

import disruptor.event.journal.JournalFileManager;
import disruptor.event.journal.persist.OutputMethodProvider;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.nio.channels.FileChannel;

public class FileChannelProvider implements OutputMethodProvider<FileChannel> {

    private JournalFileManager journalFileManager;

    public FileChannelProvider(JournalFileManager journalFileManager) {
        this.journalFileManager = journalFileManager;
    }

    @Override
    public FileChannel provide() {

        FileChannel fileChannel = null;

        try {
            fileChannel = new FileOutputStream(journalFileManager.getFilePath().toFile()).getChannel();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        return fileChannel;
    }
}

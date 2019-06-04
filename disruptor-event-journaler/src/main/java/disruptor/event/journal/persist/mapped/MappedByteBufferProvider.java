package disruptor.event.journal.persist.mapped;

import disruptor.event.journal.JournalFileManager;
import disruptor.event.journal.persist.OutputMethodProvider;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;

public class MappedByteBufferProvider implements OutputMethodProvider<MappedByteBuffer> {

    private JournalFileManager journalFileManager;
    private int mapSize;

    public MappedByteBufferProvider(JournalFileManager journalFileManager, int mapSize) {
        this.journalFileManager = journalFileManager;
        this.mapSize = mapSize;
    }

    @Override
    public MappedByteBuffer provide() {

        MappedByteBuffer mappedBuffer = null;

        try {
            mappedBuffer = new FileOutputStream(journalFileManager.getFilePath().toFile()).getChannel()
                    .map(FileChannel.MapMode.READ_WRITE, 0, mapSize);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return mappedBuffer;
    }
}

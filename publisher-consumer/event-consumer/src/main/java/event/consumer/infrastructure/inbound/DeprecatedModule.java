package event.consumer.infrastructure.inbound;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;

import java.io.*;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;

public class DeprecatedModule extends AbstractModule {

    @Provides
    ObjectOutputStream provideObjectOutputStream() {
        ObjectOutputStream objectOutputStream = null;

        try {
            File rootDir = new File("journal");
            rootDir.mkdirs();

            FileOutputStream outputStream = new FileOutputStream(new File(rootDir.toString(), "price-event.log"));
            objectOutputStream = new ObjectOutputStream(outputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return objectOutputStream;
    }


    @Provides
    MappedByteBuffer provideMappedByteByffer() {
        MappedByteBuffer mappedByteBuffer = null;

        try {
            RandomAccessFile rafile = new RandomAccessFile("MemoryMappedFile.jpg", "rw");
            rafile.setLength(1024 * 1024 * 512);
            FileChannel fileChannel = rafile.getChannel();
            mappedByteBuffer = fileChannel.map(FileChannel.MapMode.READ_WRITE, 0, 1024 * 1024 * 512);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return mappedByteBuffer;
    }
}

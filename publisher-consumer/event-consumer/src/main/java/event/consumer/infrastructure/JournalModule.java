package event.consumer.infrastructure;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import disruptor.event.journal.Journal;
import disruptor.event.journal.JournalBufferWriter;
import disruptor.event.journal.JournalBuilder;
import event.consumer.encoding.SbeBufferWriter;

public class JournalModule extends AbstractModule {

    @Override
    protected void configure() {
    }

    @Provides
    Journal provideJournal() {
        JournalBufferWriter bw = new SbeBufferWriter();

        JournalBuilder builder = JournalBuilder.create()
                .withBufferWriter(bw)
                .withJournalFileSize(10000);

        return builder.build();
    }
}


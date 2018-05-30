package hr.cleancode.message.impl;

import akka.Done;
import akka.japi.Pair;
import akka.stream.javadsl.Flow;
import com.google.inject.Inject;
import com.lightbend.lagom.javadsl.persistence.AggregateEventTag;
import com.lightbend.lagom.javadsl.persistence.Offset;
import com.lightbend.lagom.javadsl.persistence.ReadSideProcessor;
import lombok.extern.slf4j.Slf4j;
import org.pcollections.PSequence;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

import static io.vavr.API.$;
import static io.vavr.API.Case;
import static io.vavr.API.Match;
import static io.vavr.API.run;
import static io.vavr.Predicates.instanceOf;

@Slf4j
public class ChannelEventProcessor extends ReadSideProcessor<ChannelEvent> {

    @Override
    public PSequence<AggregateEventTag<ChannelEvent>> aggregateTags() {
        return ChannelEvent.TAG.allTags();
    }

    final ChannelRepository repository;

    @Inject
    public ChannelEventProcessor(final ChannelRepository repository) {
        this.repository = repository;
    }

    @Override
    public ReadSideHandler<ChannelEvent> buildHandler() {
        return new ReadSideHandler<ChannelEvent>() {
            @Override
            public Flow<Pair<ChannelEvent, Offset>, Done, ?> handle() {
                return Flow.<Pair<ChannelEvent, Offset>>create()
                        .mapAsync(1, eventAndOffset ->
                                handleEvent(eventAndOffset.first(), eventAndOffset.second())
                        );
            }
        };
    }

    private CompletionStage<Done> handleEvent(final ChannelEvent event, final Offset offset) {
        log.info("Persisting event {} at offset {}.", event, offset);
        return CompletableFuture.supplyAsync(() -> {
            Match(event).of(
                    Case($(instanceOf(ChannelEvent.ChannelCreated.class)), o -> run(() -> repository.save(event.getChannel())))
            );
            return Done.getInstance();
        });
    }



}

package hr.cleancode.message.impl;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.lightbend.lagom.javadsl.persistence.AggregateEvent;
import com.lightbend.lagom.javadsl.persistence.AggregateEventShards;
import com.lightbend.lagom.javadsl.persistence.AggregateEventTag;
import com.lightbend.lagom.javadsl.persistence.AggregateEventTagger;
import com.lightbend.lagom.serialization.Jsonable;
import hr.cleancode.message.api.Channel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Value;

public interface ChannelEvent extends Jsonable, AggregateEvent<ChannelEvent> {

    AggregateEventShards<ChannelEvent> TAG = AggregateEventTag.sharded(ChannelEvent.class, 3);

    @Override
    default AggregateEventTagger<ChannelEvent> aggregateTag() {
        return TAG;
    }

    Channel getChannel();

    @Value
    @Builder
    @AllArgsConstructor(onConstructor = @__(@JsonCreator))
    class ChannelCreated implements ChannelEvent {
        String id;
        Channel channel;
    }

}

package hr.cleancode.message.impl;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.lightbend.lagom.javadsl.persistence.AggregateEvent;
import com.lightbend.lagom.javadsl.persistence.AggregateEventShards;
import com.lightbend.lagom.javadsl.persistence.AggregateEventTag;
import com.lightbend.lagom.javadsl.persistence.AggregateEventTagger;
import com.lightbend.lagom.serialization.Jsonable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Value;

public interface UniqueLockEvent extends Jsonable, AggregateEvent<UniqueLockEvent> {

    AggregateEventShards<UniqueLockEvent> TAG = AggregateEventTag.sharded(UniqueLockEvent.class, 3);

    @Override
    default AggregateEventTagger<UniqueLockEvent> aggregateTag() {
        return TAG;
    }

    @Value
    @Builder
    @AllArgsConstructor(onConstructor = @__(@JsonCreator))
    class UniqueLockPlaced implements UniqueLockEvent {
        String id;
    }

    @Value
    @Builder
    @AllArgsConstructor(onConstructor = @__(@JsonCreator))
    class UniqueLockRemoved implements UniqueLockEvent {
        String id;
    }

}

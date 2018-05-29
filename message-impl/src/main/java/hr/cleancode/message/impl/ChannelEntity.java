package hr.cleancode.message.impl;

import akka.Done;
import com.lightbend.lagom.javadsl.api.transport.NotFound;
import com.lightbend.lagom.javadsl.persistence.PersistentEntity;
import hr.cleancode.message.api.Channel;

import java.util.Optional;

public class ChannelEntity extends PersistentEntity<ChannelCommand, ChannelEvent, ChannelState> {

    @Override
    public Behavior initialBehavior(Optional<ChannelState> snapshotState) {
        ChannelStatus status = snapshotState.map(ChannelState::getStatus).orElse(ChannelStatus.NOT_CREATED);
        switch (status) {
            case NOT_CREATED:
                return empty();
            case CREATED:
                return created(snapshotState.get());
            default:
                throw new IllegalStateException("Unknown status of channel: " + status);
        }
    }

    private Behavior empty() {
        BehaviorBuilder b = newBehaviorBuilder(ChannelState.empty());

        b.setCommandHandler(ChannelCommand.CreateChannelCommand.class, (cmd, ctx) -> {
            ChannelEvent.ChannelCreated event = ChannelEvent.ChannelCreated.builder()
                    .id(entityId())
                    .channel(cmd.getChannel())
                    .build();
            return ctx.thenPersist(event, (evt) -> ctx.reply(Done.getInstance()));
        });

        b.setEventHandlerChangingBehavior(ChannelEvent.ChannelCreated.class, evt -> created(ChannelState.created(evt.getChannel())));

        return b.build();
    }

    private Behavior created(ChannelState state) {
        BehaviorBuilder b = newBehaviorBuilder(state);

        b.setReadOnlyCommandHandler(ChannelCommand.GetChannel.class, (cmd, ctx) -> {
            Optional<Channel> result = state().getChannel();
            if (result.isPresent()) {
                ctx.reply(state().getChannel());
            } else {
                throw new NotFound("Channel not found");
            }
        });

        return b.build();
    }
}

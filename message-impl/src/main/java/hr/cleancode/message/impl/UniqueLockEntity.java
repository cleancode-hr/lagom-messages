package hr.cleancode.message.impl;

import akka.Done;
import com.lightbend.lagom.javadsl.persistence.PersistentEntity;

import java.util.Optional;

public class UniqueLockEntity extends PersistentEntity<UniqueLockCommand, UniqueLockEvent, UniqueLockState> {

    @Override
    public Behavior initialBehavior(Optional<UniqueLockState> snapshotState) {
        UniqueLockStatus status = snapshotState.map(UniqueLockState::getStatus).orElse(UniqueLockStatus.NOT_PLACED);
        switch (status) {
            case NOT_PLACED:
                return notPlaced();
            case PLACED:
                return placed(snapshotState.get());
            default:
                throw new IllegalStateException("Unknown status of channel: " + status);
        }
    }

    private Behavior notPlaced() {
        BehaviorBuilder b = newBehaviorBuilder(UniqueLockState.notPlaced());

        b.setCommandHandler(UniqueLockCommand.PlaceLockCommand.class, (cmd, ctx) -> {
            UniqueLockEvent event = new UniqueLockEvent.UniqueLockPlaced.UniqueLockPlacedBuilder()
                    .id(cmd.getLockId())
                    .build();
            return ctx.thenPersist(event, (evt) -> ctx.reply(Done.getInstance()));
        });
        b.setReadOnlyCommandHandler(UniqueLockCommand.GetLockCommand.class, (cmd, ctx) -> {
            ctx.reply("");
        });
        b.setEventHandlerChangingBehavior(UniqueLockEvent.UniqueLockPlaced.class, evt -> placed(UniqueLockState.placed(evt.getId())));
        return b.build();
    }

    private Behavior placed(UniqueLockState state) {
        BehaviorBuilder b = newBehaviorBuilder(state);
        b.setCommandHandler(UniqueLockCommand.DeleteLock.class, (cmd, ctx) -> {
            UniqueLockEvent event = new UniqueLockEvent.UniqueLockRemoved.UniqueLockRemovedBuilder()
                    .id(cmd.getLockId())
                    .build();
            return ctx.thenPersist(event, (evt) -> ctx.reply(Done.getInstance()));
        });
        b.setReadOnlyCommandHandler(UniqueLockCommand.GetLockCommand.class, (cmd, ctx) -> {
            ctx.reply(state.getLockId());
        });
        b.setEventHandlerChangingBehavior(UniqueLockEvent.UniqueLockRemoved.class, evt -> notPlaced());
        return b.build();
    }

}
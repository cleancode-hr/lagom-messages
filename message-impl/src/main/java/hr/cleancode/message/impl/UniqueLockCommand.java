package hr.cleancode.message.impl;

import akka.Done;
import com.lightbend.lagom.javadsl.persistence.PersistentEntity;
import com.lightbend.lagom.serialization.Jsonable;
import hr.cleancode.message.api.UniqueLock;
import lombok.AllArgsConstructor;
import lombok.Value;
import lombok.experimental.Wither;

import java.util.Optional;

public interface UniqueLockCommand extends Jsonable {

    @Value
    @Wither
    @AllArgsConstructor(staticName = "of")
    class GetLockCommand implements UniqueLockCommand, PersistentEntity.ReplyType<Optional<UniqueLock>> {
        String lockId;
    }

    @Value
    @Wither
    @AllArgsConstructor(staticName = "of")
    class PlaceLockCommand implements UniqueLockCommand, PersistentEntity.ReplyType<Done> {
        UniqueLock lock;
    }

    @Value
    @Wither
    @AllArgsConstructor(staticName = "of")
    class DeleteLock implements UniqueLockCommand, PersistentEntity.ReplyType<Done> {
        String lockId;
    }
}

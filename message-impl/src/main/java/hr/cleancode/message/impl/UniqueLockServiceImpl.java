package hr.cleancode.message.impl;

import akka.Done;
import com.google.inject.Inject;
import com.lightbend.lagom.javadsl.api.ServiceCall;
import com.lightbend.lagom.javadsl.api.transport.BadRequest;
import com.lightbend.lagom.javadsl.persistence.PersistentEntityRef;
import com.lightbend.lagom.javadsl.persistence.PersistentEntityRegistry;
import hr.cleancode.message.api.UniqueLock;
import hr.cleancode.message.api.UniqueLockService;

import java.util.Optional;
import java.util.concurrent.CompletionStage;

public class UniqueLockServiceImpl implements UniqueLockService {

    private final PersistentEntityRegistry persistentEntities;

    @Inject
    public UniqueLockServiceImpl(PersistentEntityRegistry persistentEntities) {
        this.persistentEntities = persistentEntities;
        persistentEntities.register(UniqueLockEntity.class);
    }

    @Override
    public ServiceCall<UniqueLock, Done> placeLock() {
        return request -> entityRef(request.getId()).ask(UniqueLockCommand.PlaceLockCommand.of(request))
                            .thenApply(ack -> Done.getInstance());
    }

    @Override
    public ServiceCall<String, UniqueLock> getLock(final String lockId) {
        return request -> readLock(lockId).thenApply(uniqueLock -> uniqueLock.get());
    }

    @Override
    public ServiceCall<String, Done> removeLock(String lockId) {
        return request -> readLock(lockId).thenCompose(uniqueLock -> {
            uniqueLock.orElseThrow(() -> new BadRequest("Unique lock failed to remove"));
            return entityRef(request).ask(UniqueLockCommand.DeleteLock.of(request)).thenApply(done -> Done.getInstance());
        });
    }

    private CompletionStage<Optional<UniqueLock>> readLock(String lockId) {
        return entityRef(lockId).ask(UniqueLockCommand.GetLockCommand.of(lockId));
    }

    private PersistentEntityRef<UniqueLockCommand> entityRef(String lockId) {
        return persistentEntities.refFor(UniqueLockEntity.class, lockId);
    }
}

package hr.cleancode.message.impl;

import akka.Done;
import com.google.inject.Inject;
import com.lightbend.lagom.javadsl.api.ServiceCall;
import com.lightbend.lagom.javadsl.api.transport.BadRequest;
import com.lightbend.lagom.javadsl.persistence.PersistentEntityRef;
import com.lightbend.lagom.javadsl.persistence.PersistentEntityRegistry;
import hr.cleancode.message.api.UniqueLock;
import hr.cleancode.message.api.UniqueLockService;

public class UniqueLockServiceImpl implements UniqueLockService {

    private final PersistentEntityRegistry persistentEntities;

    @Inject
    public UniqueLockServiceImpl(PersistentEntityRegistry persistentEntities) {
        this.persistentEntities = persistentEntities;
        persistentEntities.register(UniqueLockEntity.class);
    }

    @Override
    public ServiceCall<UniqueLock, Done> placeLock() {
        return request -> entityRef(request.getId()).ask(UniqueLockCommand.GetLockCommand.of(request.getId()))
                    .thenCompose(uniqueLock -> {
                        if (uniqueLock.isPresent()) {
                            throw new BadRequest("Unique lock failed to acquire");
                        }
                        return entityRef(request.getId()).ask(UniqueLockCommand.PlaceLockCommand.of(request))
                                .thenApply(ack -> Done.getInstance());
                    });
    }

    @Override
    public ServiceCall<String, UniqueLock> getLock(String lockId) {
        return request -> entityRef(lockId).ask(UniqueLockCommand.GetLockCommand.of(lockId)).thenApply(uniqueLock -> uniqueLock.get());
    }

    @Override
    public ServiceCall<String, String> removeLock(String lockId) {
        return request -> entityRef(lockId).ask(UniqueLockCommand.GetLockCommand.of(lockId))
                .thenCompose(uniqueLock -> {
                    uniqueLock.orElseThrow(() -> new BadRequest("Unique lock failed to remove"));
                    return entityRef(request).ask(UniqueLockCommand.DeleteLock.of(request)).thenApply(done -> request);
        });
    }

    private PersistentEntityRef<UniqueLockCommand> entityRef(String lockId) {
        return persistentEntities.refFor(UniqueLockEntity.class, lockId);
    }
}

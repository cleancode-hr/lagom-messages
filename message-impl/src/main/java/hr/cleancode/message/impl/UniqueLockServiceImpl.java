package hr.cleancode.message.impl;

import com.google.inject.Inject;
import com.lightbend.lagom.javadsl.api.ServiceCall;
import com.lightbend.lagom.javadsl.api.transport.BadRequest;
import com.lightbend.lagom.javadsl.persistence.PersistentEntityRef;
import com.lightbend.lagom.javadsl.persistence.PersistentEntityRegistry;
import hr.cleancode.message.api.UniqueLockService;
import org.apache.commons.lang3.StringUtils;

public class UniqueLockServiceImpl implements UniqueLockService {

    private final PersistentEntityRegistry persistentEntities;

    @Inject
    public UniqueLockServiceImpl(PersistentEntityRegistry persistentEntities) {
        this.persistentEntities = persistentEntities;
        persistentEntities.register(UniqueLockEntity.class);
    }

    @Override
    public ServiceCall<String, String> placeLock(String lockId) {
        return request -> getLock(request).invoke(request).thenCompose(existingLockId -> {
            if (StringUtils.isNotBlank(existingLockId)) {
                throw new BadRequest("Unique lock failed to acquire");
            }
            return entityRef(request).ask(UniqueLockCommand.PlaceLockCommand.of(request)).thenApply(done -> request);
        });
    }

    @Override
    public ServiceCall<String, String> getLock(String lockId) {
        return request -> entityRef(lockId).ask(UniqueLockCommand.GetLockCommand.of(lockId));
    }

    @Override
    public ServiceCall<String, String> removeLock(String lockId) {
        return request -> getLock(request).invoke(request).thenCompose(existingLockId -> {
            if (existingLockId == null) {
                throw new IllegalArgumentException("Unique lock failed to remove");
            }
            return entityRef(request).ask(UniqueLockCommand.DeleteLock.of(request)).thenApply(done -> request);
        });
    }

    private PersistentEntityRef<UniqueLockCommand> entityRef(String lockId) {
        return persistentEntities.refFor(UniqueLockEntity.class, lockId);
    }
}

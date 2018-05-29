package hr.cleancode.message.impl;

import akka.NotUsed;
import com.google.inject.Inject;
import com.lightbend.lagom.javadsl.api.ServiceCall;
import com.lightbend.lagom.javadsl.persistence.PersistentEntityRef;
import com.lightbend.lagom.javadsl.persistence.PersistentEntityRegistry;
import hr.cleancode.message.api.Channel;
import hr.cleancode.message.api.ChannelCreate;
import hr.cleancode.message.api.ChannelService;
import hr.cleancode.message.api.UniqueLockService;

import java.time.ZonedDateTime;
import java.util.Optional;
import java.util.UUID;

public class ChannelServiceImpl implements ChannelService {

    private final PersistentEntityRegistry persistentEntities;

    private final UniqueLockService uniqueLockService;

    @Inject
    public ChannelServiceImpl(PersistentEntityRegistry persistentEntities, UniqueLockService uniqueLockService) {
        this.persistentEntities = persistentEntities;
        this.uniqueLockService = uniqueLockService;
        persistentEntities.register(ChannelEntity.class);
    }

    @Override
    public ServiceCall<ChannelCreate, Channel> createChannel() {
        return request -> {
            final UUID channelId = UUID.randomUUID();
            final Channel channel = new Channel(channelId, request.getCreatedId(), ZonedDateTime.now(), request.getTitle());
            final ChannelCommand.CreateChannelCommand cmd = ChannelCommand.CreateChannelCommand.of(channel);
            PersistentEntityRef<ChannelCommand> ref = entityRef(channelId);
            return uniqueLockService
                    .placeLock(request.getTitle()).invoke(request.getTitle())
                    .thenCompose(lockId -> ref.ask(cmd).thenApply(done -> channel));
        };
    }

    @Override
    public ServiceCall<NotUsed, Optional<Channel>> getChannel(UUID id) {
        return request -> entityRef(id).ask(ChannelCommand.GetChannel.of(id));
    }

    private PersistentEntityRef<ChannelCommand> entityRef(UUID channelId) {
        return persistentEntities.refFor(ChannelEntity.class, channelId.toString());
    }

}

package hr.cleancode.message.impl;

import akka.NotUsed;
import com.google.inject.Inject;
import com.lightbend.lagom.javadsl.api.ServiceCall;
import com.lightbend.lagom.javadsl.persistence.PersistentEntityRef;
import com.lightbend.lagom.javadsl.persistence.PersistentEntityRegistry;
import com.lightbend.lagom.javadsl.persistence.ReadSide;
import hr.cleancode.message.api.Channel;
import hr.cleancode.message.api.ChannelCreate;
import hr.cleancode.message.api.ChannelService;
import hr.cleancode.message.api.UniqueLock;
import hr.cleancode.message.api.UniqueLockService;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public class ChannelServiceImpl implements ChannelService {

    private final PersistentEntityRegistry persistentEntities;

    private final UniqueLockService uniqueLockService;

    private final ChannelRepository channelRepository;

    @Inject
    public ChannelServiceImpl(final ReadSide readSide, PersistentEntityRegistry persistentEntities, UniqueLockService uniqueLockService, ChannelRepository channelRepository) {
        this.persistentEntities = persistentEntities;
        this.uniqueLockService = uniqueLockService;
        this.channelRepository = channelRepository;
        persistentEntities.register(ChannelEntity.class);
        readSide.register(ChannelEventProcessor.class);
    }

    @Override
    public ServiceCall<ChannelCreate, Channel> createChannel() {
        return request -> {
            final UUID channelId = UUID.randomUUID();
            final Channel channel = new Channel(channelId, request.getCreatedId(), ZonedDateTime.now(), request.getTitle());
            final ChannelCommand.CreateChannelCommand createChannelCommand = ChannelCommand.CreateChannelCommand.of(channel);
            final PersistentEntityRef<ChannelCommand> channelEntity = entityRef(channelId);
            final UniqueLock uniqueTitleLock = UniqueLock.forValues(Channel.class, channel.getTitle());
            return uniqueLockService
                    .placeLock().invoke(uniqueTitleLock)
                    .thenCompose(lockId -> channelEntity.ask(createChannelCommand).thenApply(done -> channel));
        };
    }

    @Override
    public ServiceCall<NotUsed, Optional<Channel>> getChannel(UUID id) {
        return request -> entityRef(id).ask(ChannelCommand.GetChannel.of(id));
    }

    @Override
    public ServiceCall<NotUsed, List<Channel>> getChannels() {
        return request -> CompletableFuture.supplyAsync(() -> channelRepository.findAll());
    }

    private PersistentEntityRef<ChannelCommand> entityRef(UUID channelId) {
        return persistentEntities.refFor(ChannelEntity.class, channelId.toString());
    }

}

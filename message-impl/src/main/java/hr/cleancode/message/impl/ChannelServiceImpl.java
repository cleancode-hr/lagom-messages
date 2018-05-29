package hr.cleancode.message.impl;

import akka.NotUsed;
import com.google.inject.Inject;
import com.lightbend.lagom.javadsl.api.ServiceCall;
import com.lightbend.lagom.javadsl.persistence.PersistentEntityRef;
import com.lightbend.lagom.javadsl.persistence.PersistentEntityRegistry;
import hr.cleancode.message.api.Channel;
import hr.cleancode.message.api.ChannelCreate;
import hr.cleancode.message.api.ChannelService;

import java.time.ZonedDateTime;
import java.util.Optional;
import java.util.UUID;

public class ChannelServiceImpl implements ChannelService {

    private final PersistentEntityRegistry persistentEntities;

    @Inject
    public ChannelServiceImpl(PersistentEntityRegistry persistentEntities) {
        this.persistentEntities = persistentEntities;
        persistentEntities.register(ChannelEntity.class);
    }

    @Override
    public ServiceCall<ChannelCreate, Channel> createChannel() {
        return request -> {
            final UUID channelId = UUID.randomUUID();
            Channel channel = new Channel(channelId, request.getCreatedId(), ZonedDateTime.now(), request.getTitle());
            ChannelCommand.CreateChannelCommand cmd = ChannelCommand.CreateChannelCommand.of(channel);
            return entityRef(channelId).ask(cmd).thenApply(done -> channel);
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

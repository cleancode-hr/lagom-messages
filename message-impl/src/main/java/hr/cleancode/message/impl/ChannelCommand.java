package hr.cleancode.message.impl;

import akka.Done;
import com.lightbend.lagom.javadsl.persistence.PersistentEntity;
import com.lightbend.lagom.serialization.Jsonable;
import hr.cleancode.message.api.Channel;
import lombok.AllArgsConstructor;
import lombok.Value;
import lombok.experimental.Wither;

import java.util.Optional;
import java.util.UUID;

public interface ChannelCommand extends Jsonable {

    @Value
    @Wither
    @AllArgsConstructor(staticName = "of")
    class CreateChannelCommand implements ChannelCommand, PersistentEntity.ReplyType<Done> {
        Channel channel;
    }

    @Value
    @Wither
    @AllArgsConstructor(staticName = "of")
    class GetChannel implements ChannelCommand, PersistentEntity.ReplyType<Optional<Channel>> {
        UUID channelId;
    }
}

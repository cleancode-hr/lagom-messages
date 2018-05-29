package hr.cleancode.message.api;

import akka.NotUsed;
import akka.util.ByteString;
import com.lightbend.lagom.javadsl.api.Descriptor;
import com.lightbend.lagom.javadsl.api.Service;
import com.lightbend.lagom.javadsl.api.ServiceCall;
import com.lightbend.lagom.javadsl.api.deser.ExceptionSerializer;
import com.lightbend.lagom.javadsl.api.deser.PathParamSerializers;
import com.lightbend.lagom.javadsl.api.deser.RawExceptionMessage;
import com.lightbend.lagom.javadsl.api.transport.MessageProtocol;
import com.lightbend.lagom.javadsl.api.transport.Method;
import com.lightbend.lagom.javadsl.api.transport.TransportErrorCode;

import java.util.Collection;
import java.util.Optional;
import java.util.UUID;

import static com.lightbend.lagom.javadsl.api.Service.named;
import static com.lightbend.lagom.javadsl.api.Service.restCall;

public interface ChannelService extends Service {

    ServiceCall<ChannelCreate, Channel> createChannel();

    ServiceCall<NotUsed, Optional<Channel>> getChannel(UUID id);

    @Override
    default Descriptor descriptor() {
        return named("channelService").withCalls(
                restCall(Method.POST, "/channels", this::createChannel),
                restCall(Method.GET, "/channels/:channelId", this::getChannel)
        ).withAutoAcl(true)
                .withPathParamSerializer(
                        UUID.class, PathParamSerializers.required("UUID", UUID::fromString, UUID::toString));
    }
}

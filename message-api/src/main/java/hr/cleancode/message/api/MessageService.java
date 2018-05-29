package hr.cleancode.message.api;

import akka.NotUsed;
import com.lightbend.lagom.javadsl.api.Descriptor;
import com.lightbend.lagom.javadsl.api.Service;
import com.lightbend.lagom.javadsl.api.ServiceCall;
import com.lightbend.lagom.javadsl.api.deser.PathParamSerializers;
import com.lightbend.lagom.javadsl.api.transport.Method;

import java.util.Optional;
import java.util.UUID;

import static com.lightbend.lagom.javadsl.api.Service.named;
import static com.lightbend.lagom.javadsl.api.Service.restCall;

public interface MessageService extends Service {

    ServiceCall<MessageDto, String> createMessage();

    ServiceCall<NotUsed, Optional<MessageDto>> getMessage(UUID id);

    @Override
    default Descriptor descriptor() {
        return named("messageService").withCalls(
                restCall(Method.POST, "/channels/:channelId/messages", this::createMessage),
                restCall(Method.GET, "/channels/:channelId/messages/:messageId", this::getMessage)
        ).withAutoAcl(true)
                .withPathParamSerializer(
                        UUID.class, PathParamSerializers.required("UUID", UUID::fromString, UUID::toString));
    }
}

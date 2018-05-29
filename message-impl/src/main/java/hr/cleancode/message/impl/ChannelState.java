package hr.cleancode.message.impl;

import com.fasterxml.jackson.annotation.JsonCreator;
import hr.cleancode.message.api.Channel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Value;
import lombok.experimental.Wither;

import java.util.Optional;

@Value
@Wither
@Builder
@AllArgsConstructor(onConstructor = @__(@JsonCreator))
public class ChannelState {

    private Optional<Channel> channel;

    private ChannelStatus status;

    public static ChannelState empty() {
        return new ChannelState(Optional.empty(), ChannelStatus.NOT_CREATED);
    }

    public static ChannelState created(Channel channel) {
        return new ChannelState(Optional.of(channel), ChannelStatus.CREATED);

    }
}

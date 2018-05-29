package hr.cleancode.message.api;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Value;

@Value
@JsonDeserialize
@Builder
@AllArgsConstructor
public final class ChannelCreate {
    final private String title;
    final private String createdId;
}

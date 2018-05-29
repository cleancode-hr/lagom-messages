package hr.cleancode.message.api;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Value;

import java.time.ZonedDateTime;
import java.util.UUID;

@Value
@JsonDeserialize
@Builder
@AllArgsConstructor
public final class Channel {
    final private UUID id;
    final private String createdId;
    final private ZonedDateTime createdAt;
    final private String title;
}

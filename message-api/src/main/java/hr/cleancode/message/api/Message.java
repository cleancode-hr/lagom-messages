package hr.cleancode.message.api;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Value;

import java.time.ZonedDateTime;
import java.util.UUID;

@Value
@JsonDeserialize
@Builder
@AllArgsConstructor
public class Message {
    private UUID id;
    private String senderId;
    private ZonedDateTime sentAt;
    private String subject;
    private String content;
}

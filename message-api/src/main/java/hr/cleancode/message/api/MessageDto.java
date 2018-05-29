package hr.cleancode.message.api;

import lombok.Data;

import java.time.ZonedDateTime;
import java.util.UUID;

@Data
public class MessageDto {
    private UUID id;
    private String senderId;
    private ZonedDateTime sentAt;
    private String subject;
    private String content;
}

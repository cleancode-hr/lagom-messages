package hr.cleancode.message.api;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import joptsimple.internal.Strings;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Value;

import java.time.ZonedDateTime;

@Value
@JsonDeserialize
@Builder
@AllArgsConstructor
public class UniqueLock {
    private String id;
    private ZonedDateTime createdAt;

    public static String determineId(String...parts) {
        return Strings.join(parts, "#");
    }
}

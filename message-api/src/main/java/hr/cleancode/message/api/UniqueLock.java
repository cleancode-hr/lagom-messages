package hr.cleancode.message.api;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import joptsimple.internal.Strings;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Value;

@Value
@JsonDeserialize
@Builder
@AllArgsConstructor
public class UniqueLock {
    private String id;

    public static UniqueLock of(String id) {
        return new UniqueLock(id);
    }

    public static UniqueLock forValues(String entityName, String...values) {
        return new UniqueLock(entityName + "#" + Strings.join(values, "#"));
    }

    public static UniqueLock forValues(Class<?> entityClass, String...values) {
        return forValues(entityClass.getName(), values);
    }
}

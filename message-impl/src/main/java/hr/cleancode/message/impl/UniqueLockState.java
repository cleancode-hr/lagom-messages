package hr.cleancode.message.impl;

import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Value;
import lombok.experimental.Wither;

@Value
@Wither
@Builder
@AllArgsConstructor(onConstructor = @__(@JsonCreator))
public class UniqueLockState {

    private String lockId;

    private UniqueLockStatus status;

    public static UniqueLockState notPlaced() {
        return new UniqueLockState(null, UniqueLockStatus.NOT_PLACED);
    }

    public static UniqueLockState placed(String lockId) {
        return new UniqueLockState(lockId, UniqueLockStatus.PLACED);
    }
}

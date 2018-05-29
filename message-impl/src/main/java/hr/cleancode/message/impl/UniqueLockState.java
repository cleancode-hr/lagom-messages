package hr.cleancode.message.impl;

import com.fasterxml.jackson.annotation.JsonCreator;
import hr.cleancode.message.api.UniqueLock;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Value;
import lombok.experimental.Wither;

@Value
@Wither
@Builder
@AllArgsConstructor(onConstructor = @__(@JsonCreator))
public class UniqueLockState {

    private UniqueLock lock;

    private UniqueLockStatus status;

    public static UniqueLockState notPlaced() {
        return new UniqueLockState(null, UniqueLockStatus.NOT_PLACED);
    }

    public static UniqueLockState placed(UniqueLock lock) {
        return new UniqueLockState(lock, UniqueLockStatus.PLACED);
    }
}

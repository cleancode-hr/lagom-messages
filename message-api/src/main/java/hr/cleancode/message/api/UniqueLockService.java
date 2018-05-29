package hr.cleancode.message.api;

import com.lightbend.lagom.javadsl.api.Descriptor;
import com.lightbend.lagom.javadsl.api.Service;
import com.lightbend.lagom.javadsl.api.ServiceCall;
import com.lightbend.lagom.javadsl.api.transport.Method;

import static com.lightbend.lagom.javadsl.api.Service.named;
import static com.lightbend.lagom.javadsl.api.Service.restCall;

public interface UniqueLockService extends Service {

    ServiceCall<String, String> placeLock(String lockId);

    ServiceCall<String, String> getLock(String lockId);

    ServiceCall<String, String> removeLock(String lockId);

    @Override
    default Descriptor descriptor() {
        return named("uniqueLockService").withCalls(
                restCall(Method.POST, "/locks", this::placeLock),
                restCall(Method.GET, "/locks/:lockId", this::getLock),
                restCall(Method.DELETE, "/locks/:lockId", this::removeLock)
        ).withAutoAcl(true);
    }
}

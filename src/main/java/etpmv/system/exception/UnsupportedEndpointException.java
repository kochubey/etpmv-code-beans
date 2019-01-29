package etpmv.system.exception;

import java.util.Set;

public class UnsupportedEndpointException extends PtsOtherException {
    public UnsupportedEndpointException(String endpointId, Set<String> allowedSet) {
        super("Unsupported endpoint key: [" + endpointId + "] allowed: " + allowedSet);
    }
}

package etpmv.system.exception;

import java.util.List;

public class BadEndpointConfigurationException extends PtsOtherException {
    public BadEndpointConfigurationException(List<String> emptyFields) {
        super("Bad endpoint configuration, no values found for property: " + emptyFields);
    }
}

package etpmv.system.exception.runtime;

public class ContextFileNotFoundException extends RuntimeException {
    public ContextFileNotFoundException(String configFilePath) {
        super("Context file not found by path: " + configFilePath);
    }
}

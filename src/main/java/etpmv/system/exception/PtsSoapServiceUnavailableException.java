package etpmv.system.exception;

public class PtsSoapServiceUnavailableException extends PtsOtherException {
    public PtsSoapServiceUnavailableException(String serviceUrl) {
        super(String.format("SOAP не доступен по URL: %s", serviceUrl));
    }
}

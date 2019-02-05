package etpmv.canon.code.exceptions;

public class ConnectionTimeoutException extends EtpmvException {

    private static final String MESSAGE_CODE = "1016";
    private static final String RESULT = "Получатель недоступен.";
    private static final String DESCRIPTION = "ПТС не доступна по адресу, указанному в Реестре ПТС. ";

    public ConnectionTimeoutException(String endpointUrl) {
        super(MESSAGE_CODE, RESULT, String.format("%s (%s)",DESCRIPTION, endpointUrl));
    }
}

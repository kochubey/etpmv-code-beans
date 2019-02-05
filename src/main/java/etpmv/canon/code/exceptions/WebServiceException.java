package etpmv.canon.code.exceptions;

public class WebServiceException extends EtpmvException {

    private static final String RESULT = "Ошибка веб - сервиса получателя сообщения";

    public WebServiceException(String code, String description) {
        super(code, RESULT, description);
    }
}

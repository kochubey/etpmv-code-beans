package etpmv.canon.code.exceptions;

public class HeadersFormatException extends EtpmvException {

    private static final String MESSAGE_CODE = "1005";
    private static final String RESULT = "Нарушен формат идентификаторов сообщения";
    private static final String DESCRIPTION = "Формат идентификаторов сообщения X-Data-Source X-Request-Id X-Response-Id не соответствует стандарту: {urn:pts:ptsCode:exchangeId}";

    public HeadersFormatException() {
        super(MESSAGE_CODE, RESULT, DESCRIPTION);
    }
}

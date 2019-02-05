package etpmv.canon.code.exceptions;

public class MissingHeadersException extends EtpmvException {

    private static final String MESSAGE_CODE = "1004";
    private static final String RESULT = "Отсутствуют необходимые заголовки сообщения";
    private static final String DESCRIPTION = "Не указан один из заголовков сообщения: X-Data-Source, X-Request-Id, X-Message-Type, MessageText";

    public MissingHeadersException() {
        super(MESSAGE_CODE, RESULT, DESCRIPTION);
    }
}

package etpmv.canon.code.exceptions;

public class MessageSizeLimitException extends EtpmvException {

    private static final String MESSAGE_CODE = "1007";
    private static final String RESULT = "Превышен максимальный размер передаваемых данных.";
    private static final String DESCRIPTION = "Превышен максимально допустимый размер передаваемых бизнес-данных сообщения. " +
            "Размер тела сообщения превысил 100Мб";

    public MessageSizeLimitException() {
        super(MESSAGE_CODE, RESULT, DESCRIPTION);
    }
}

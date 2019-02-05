package etpmv.canon.code.exceptions;

public class EmptyMessageException extends EtpmvException {

    private static final String MESSAGE_CODE = "1008";
    private static final String RESULT = "Получено пустое сообщение";
    private static final String DESCRIPTION = "Сообщение не содержит бизнес-данных. Размер тела сообщения равен нулю";

    public EmptyMessageException() {
        super(MESSAGE_CODE, RESULT, DESCRIPTION);
    }
}

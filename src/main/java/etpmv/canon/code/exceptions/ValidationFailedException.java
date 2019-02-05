package etpmv.canon.code.exceptions;

public class ValidationFailedException extends EtpmvException {

    private static final String MESSAGE_CODE = "1012";
    private static final String RESULT = "Формат бизнес-данных сообщения не соответствует схеме";
    private static final String DESCRIPTION = "Формат бизнес-данных не соответствует опубликованным  XML-схемам ВС.";

    public ValidationFailedException() {
        super(MESSAGE_CODE, RESULT, DESCRIPTION);
    }
}

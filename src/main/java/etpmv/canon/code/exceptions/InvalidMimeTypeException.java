package etpmv.canon.code.exceptions;

public class InvalidMimeTypeException extends EtpmvException {

    private static final String MESSAGE_CODE = "1003";
    private static final String RESULT = "Формат конверта не поддерживается.";
    private static final String DESCRIPTION = "MIME-тип сообщения  должен соответствовать  формату multipart/form-data";

    public InvalidMimeTypeException() {
        super(MESSAGE_CODE, RESULT, DESCRIPTION);
    }
}

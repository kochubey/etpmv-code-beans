package etpmv.canon.code.exceptions;

public class AttachmentSizeLimitException extends EtpmvException {

    private static final String MESSAGE_CODE = "1002";
    private static final String RESULT = "Превышен максимально допустимый суммарный размер присоединённых файлов.";
    private static final String DESCRIPTION = "Размер присоединённых файлов превысил 1 Гб.";

    public AttachmentSizeLimitException() {
        super(MESSAGE_CODE, RESULT, DESCRIPTION);
    }
}

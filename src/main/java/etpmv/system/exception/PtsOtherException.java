package etpmv.system.exception;

public class PtsOtherException extends BaseEsbException {
    private static final int EXCEPTION_CODE = 1116;
    private static final String EXCEPTION_MESSAGE = "Ошибка ПТС";

    public PtsOtherException(String details) {
        this(EXCEPTION_CODE, EXCEPTION_MESSAGE, details);
    }

    private PtsOtherException(int code, String message, String details) {
        super(code, message, details);
    }
}

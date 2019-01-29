package etpmv.system.exception;

public class PtsConsistentException extends BaseEsbException {
    private static final int EXCEPTION_CODE = 1117;
    private static final String EXCEPTION_MESSAGE = "Неуспешное сохранение данных в БД";

    public PtsConsistentException(String details) {
        this(EXCEPTION_CODE, EXCEPTION_MESSAGE, details);
    }

    private PtsConsistentException(int code, String message, String details) {
        super(code, message, details);
    }
}

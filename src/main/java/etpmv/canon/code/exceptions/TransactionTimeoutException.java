package etpmv.canon.code.exceptions;

public class TransactionTimeoutException extends EtpmvException {
    private static final String MESSAGE_CODE = "1017";
    private static final String RESULT = "Превышено время ожидания ответа";
    private static final String DESCRIPTION = "Ответ на запрос получен с превышением, установленного в ПТС ШОД периода ожидания";

    public TransactionTimeoutException() {
        super(MESSAGE_CODE, RESULT, DESCRIPTION);
    }
}
package etpmv.canon.code.exceptions;


public class DuplicateTransactionException  extends EtpmvException {
    private static final String MESSAGE_CODE = "1018";
    private static final String RESULT = "Сообщение дублирует ранее полученное сообщение";
    private static final String DESCRIPTION = "Сообщение с этим реквизитами было получено ранее в ПТС ШОД";

    public DuplicateTransactionException() {
        super(MESSAGE_CODE, RESULT, DESCRIPTION);
    }
}
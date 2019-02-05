package etpmv.canon.code.exceptions;

public class NoSubscribesException extends EtpmvException {

    private static final String MESSAGE_CODE = "1021";
    private static final String RESULT = "Подписанты не найдены";
    private static final String DESCRIPTION = "На данный вид сведений не подписано ни одного абонента";

    public NoSubscribesException() {
        super(MESSAGE_CODE, RESULT, DESCRIPTION);
    }
}

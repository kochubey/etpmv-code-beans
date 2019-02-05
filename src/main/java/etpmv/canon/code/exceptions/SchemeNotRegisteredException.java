package etpmv.canon.code.exceptions;

public class SchemeNotRegisteredException extends EtpmvException {

    private static final String MESSAGE_CODE = "1010";
    private static final String RESULT = "Вид сведений не зарегистрирован";
    private static final String DESCRIPTION = "ВС не зарегистрирован в Реестре ВС.";

    public SchemeNotRegisteredException() {
        super(MESSAGE_CODE, RESULT, DESCRIPTION);
    }
}

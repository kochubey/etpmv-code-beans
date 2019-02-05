package etpmv.canon.code.exceptions;

public class NotRegisteredException extends EtpmvException {

    private static final String MESSAGE_CODE = "1019";
    private static final String RESULT = "ПТС %s не зарегистрирована";
    private static final String DESCRIPTION = "Участник взаимодействия не зарегистрирован в Реестре ПТС  качестве абонента.";

    public NotRegisteredException(String ptsId) {
        super(MESSAGE_CODE, String.format(RESULT, ptsId), DESCRIPTION);
    }
}

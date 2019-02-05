package etpmv.canon.code.exceptions;

public class NotSubscribedException extends EtpmvException {

    private static final String MESSAGE_CODE = "1011";
    private static final String RESULT = "Абонент не подписан на вид сведений";
    private static final String DESCRIPTION = "В реестре ПТС ШОД для данного вида сведений не найден ptsCode Вашей ПТС в качестве подписчика";

    public NotSubscribedException() {
        super(MESSAGE_CODE, RESULT, DESCRIPTION);
    }
}

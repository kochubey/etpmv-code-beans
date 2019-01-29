package etpmv.system.exception;

public class UnsupportedMessageTextTypeException extends PtsOtherException {
    public UnsupportedMessageTextTypeException(Object messageTextType) {
        super(String.format("Не поддерживаемый тип параметра [MessageText]: %s", messageTextType.getClass().getName()));
    }
}

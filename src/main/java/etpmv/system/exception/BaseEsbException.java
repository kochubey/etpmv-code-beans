package etpmv.system.exception;



public class BaseEsbException extends Exception{
    private int code;
    private String details;
    private String message;

    public BaseEsbException(int code, String message, String details) {
        this(message);
        this.code = code;
        this.details = details;
        this.message = message;
    }

    private BaseEsbException(String message) {
        super(message);
    }

    public int getCode() {
        return code;
    }

    public String getDetails() {
        return details;
    }

//    public StatusType toStatusType() {
//        StatusType status = new StatusType();
//        status.setCode(code);
//        status.setResult(message);
//        status.setDescription(details);
//        return status;
//    }
}

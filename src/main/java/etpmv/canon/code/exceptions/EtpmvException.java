package etpmv.canon.code.exceptions;


public class EtpmvException extends Exception {
	/**
	 * 
	 */
	private static final long serialVersionUID = -8702520413703063679L;
	
	private String messageCode;
	private String messageResult;
	private String messageDescription;
	
	public EtpmvException(String messageCode, String messageResult, String messageDescription) {
		super(messageDescription);
		setMessageCode(messageCode);
		setMessageResult(messageResult);
		setMessageDescription(messageDescription);		
	}
	
	public String getMessageCode() {
		return messageCode;
	}
	private void setMessageCode(String messageCode) {
		this.messageCode = messageCode;
	}
	public String getMessageResult() {
		return messageResult;
	}
	private void setMessageResult(String messageResult) {
		this.messageResult = messageResult;
	}
	public String getMessageDescription() {
		return messageDescription;
	}
	private void setMessageDescription(String messageDescription) {
		this.messageDescription = messageDescription;
	}
	
}

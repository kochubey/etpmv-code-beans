package etpmv.canon.code.exceptions;

public class EtpmvException extends Exception {
	private static final long serialVersionUID = -8702520413703063679L;
	
	private String code;
	private String result;
	private String desc;
	
	public EtpmvException(String code, String result, String desc) {
		super(desc);
		setCode(code);
		setResult(result);
		setDesc(desc);
	}
	
	public String getCode() {
		return code;
	}
	private void setCode(String code) {
		this.code = code;
	}
	public String getResult() {
		return result;
	}
	private void setResult(String result) {
		this.result = result;
	}
	public String getDesc() {
		return desc;
	}
	private void setDesc(String desc) {
		this.desc = desc;
	}

}

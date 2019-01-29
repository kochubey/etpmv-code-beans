package etpmv.system.exception;

import org.apache.cxf.binding.soap.SoapFault;

public class PtsSoapWsFaultException extends PtsConsistentException {
    public PtsSoapWsFaultException(SoapFault exception) {
        super(String.format("Ответ SOAP сервиса ПЦП: %s", exception.getReason()));
    }
}

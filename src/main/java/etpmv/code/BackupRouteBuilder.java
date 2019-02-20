package etpmv.code;

import etpmv.canon.code.exceptions.builder.ExceptionBuilder;

public class BackupRouteBuilder extends TalendRouteBuilder {

    public String backup() {
        return "message.bk";
    }

    @Override
    public void onExceptions() {
        ExceptionBuilder.setup(this, backup());
    }

}

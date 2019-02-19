package etpmv.canon.code;

import org.apache.camel.Exchange;
import org.apache.camel.Message;
import org.apache.camel.spi.ExchangeFormatter;

import static java.lang.String.valueOf;
import static java.util.Optional.ofNullable;
import static java.util.UUID.randomUUID;

public class DataSourcePackFormatter implements ExchangeFormatter {

    private Boolean showBody;

    public DataSourcePackFormatter() {
        this(false);
    }

    public DataSourcePackFormatter(Boolean showBody) {
        this.showBody = showBody;
    }

    private String $(String format, Object... args) {
        return String.format(format, args);
    }

    @Override
    public String format(Exchange e) {
        Message message = e.getIn();
        return $(
                "ID %s| X-Exchange-Id %s| X-Request-Id %s| X-Response-Id %s| X-Data-Source %s| X-MessageType %s| X-Seq-Num %s| X-Is-Last %s| X-Event-Desc %s|%s",
                randomUUID(),
                message.getHeader("X-Exchange-Id", String.class),
                message.getHeader("X-Request-Id", String.class),
                ofNullable(message.getHeader("X-Response-Id", String.class)).orElse(""),
                message.getHeader("X-Data-Source", String.class),
                message.getHeader("X-Message-Type", String.class),
                ofNullable(message.getHeader("X-Seq-Num", String.class)).orElse(""),
                ofNullable(message.getHeader("X-Is-Last", String.class)).orElse(""),
                ofNullable(e.getProperty("X-Event-Desc", String.class)).orElse(""),
                showBody ? $("Body %s", valueOf(e.getIn().getBody())) : "");
    }

    public void setShowBody(Boolean showBody) {
        this.showBody = showBody;
    }
}
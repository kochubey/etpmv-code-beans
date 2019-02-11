package etpmv.canon.code;

import org.apache.camel.Exchange;
import org.apache.camel.Message;
import org.apache.camel.spi.ExchangeFormatter;

import java.util.Optional;

import static java.util.Optional.ofNullable;
import static java.util.UUID.randomUUID;

public class DataSourcePackFormatter implements ExchangeFormatter {

    private Boolean showBody;
    private String body;

    public DataSourcePackFormatter() {
        this(false, "#", "");
    }

    public DataSourcePackFormatter(Boolean showBody, String body, String event) {
        this.showBody = showBody;
        this.body = body;
    }

    @Override
    public String format(Exchange e) {
        Message message = e.getIn();

        if (showBody != null) {
            body = String.format("Body %s", String.valueOf(e.getIn().getBody()));
        }

        return String.format(
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
                body);
    }

    public void setShowBody(Boolean showBody) {
        this.showBody = showBody;
    }

}
package etpmv.canon.code;

import org.apache.camel.Exchange;
import org.apache.camel.Message;
import org.apache.camel.spi.ExchangeFormatter;

import java.util.Optional;

public class EtpmvLogFormatter implements ExchangeFormatter {

    private Boolean needBody;

    @Override
    public String format(Exchange exchange) {
        Message message = exchange.getIn();

        String body = "";
        if (needBody!=null) {
            body = String.format("Body %s", exchange.getIn().getBody().toString());
        }

        return String.format(
                "ID %s| X-Exchange-Id %s| X-Request-Id %s| X-Response-Id %s| X-Data-Source %s| X-MessageType %s| X-Seq-Num %s| X-Is-Last %s| X-Event-Desc %s|%s",
                exchange.getProperty("LogId", String.class),
                message.getHeader("EsbExchangeId", String.class),
                message.getHeader("X-Request-Id", String.class),
                Optional.ofNullable(message.getHeader("X-Response-Id", String.class)).orElse(""),
                message.getHeader("X-Data-Source", String.class),
                message.getHeader("X-Message-Type", String.class),
                Optional.ofNullable(message.getHeader("X-Seq-Num", String.class)).orElse(""),
                Optional.ofNullable(message.getHeader("X-Is-Last", String.class)).orElse(""),
                exchange.getProperty("X-Event-Desc", String.class),
                body);
    }

    public void setNeedBody(Boolean needBody) {
        this.needBody = needBody;
    }
}
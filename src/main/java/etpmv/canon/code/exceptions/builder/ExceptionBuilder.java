package etpmv.canon.code.exceptions.builder;

import etpmv.canon.code.exceptions.EtpmvException;
import org.apache.camel.builder.RouteBuilder;

public class ExceptionBuilder {

    public static void setup(RouteBuilder routeBuilder, String bkQueueName) {
        routeBuilder.onException(Exception.class)
                .handled(true)
                .routeId("Exception")
                .setProperty("MsgExceptionCode").constant("1020")
                .setProperty("MsgExceptionResult").constant("Ошибка ПТС ШОД. Обратитесь в службу технической поддержки.")
                .setProperty("MsgExceptionDescription").simple("${exception.stacktrace}")
                .to("direct:toBkQueue");

        routeBuilder.onException(EtpmvException.class)
                .handled(true)
                .routeId("EtpmvException")
                .setProperty("MsgExceptionCode").simple("${exception.messageCode}")
                .setProperty("MsgExceptionResult").simple("${exception.messageResult}")
                .setProperty("MsgExceptionDescription").simple("${exception.messageDescription}")
                .to("direct:toBkQueue");

        routeBuilder.from("direct:toBkQueue")
                .routeId("toBkQueue")
                .convertBodyTo(String.class)
                .setHeader("X-Error-Description").simple("<Status xmlns=\"urn://dts/shod/exchange/v1_1\">" +
                "<Code>${property.MsgExceptionCode}</Code>" +
                "<Result>${property.MsgExceptionResult}</Result>" +
                "<Description>${property.MsgExceptionDescription}</Description>" +
                "</Status>")
                .to(String.format("activemq:queue:%s", bkQueueName));
    }
}

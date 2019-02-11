package etpmv.canon.code.exceptions.builder;

import etpmv.canon.code.exceptions.EtpmvException;
import org.apache.camel.builder.RouteBuilder;

public class ExceptionBuilder {

    // todo ExceptionBuilder должен возвращать маоршрут, на то он и билдер
    // если оставить метод setup - тогда нужно по-другому назвать класс
    public static void setup(RouteBuilder routeBuilder, String bkQueueName) {
        routeBuilder.onException(Exception.class)
                .handled(true)
                .routeId("Exception")
                .setProperty("X-Err-Code").constant("1020")
                .setProperty("X-Err-Result").constant("Ошибка ПТС ШОД. Обратитесь в службу технической поддержки.")
                .setProperty("X-Err-Desc").simple("${exception.stacktrace}")
                .to("direct:toBkQueue");

        routeBuilder.onException(EtpmvException.class)
                .handled(true)
                .routeId("EtpmvException")
                .setProperty("X-Err-Code").simple("${exception.code}")
                .setProperty("X-Err-Result").simple("${exception.result}")
                .setProperty("X-Err-Desc").simple("${exception.desc}")
                .to("direct:toBkQueue");

        routeBuilder.from("direct:toBkQueue")
                .routeId("toBkQueue")
                .convertBodyTo(String.class)
                .setHeader("X-Error-Description").simple("<Status xmlns=\"urn://dts/shod/exchange/v1_1\">" +
                "<Code>${property.X-Err-Code}</Code>" +
                "<Result>${property.X-Err-Result}</Result>" +
                "<Description>${property.X-Err-Desc}</Description>" +
                "</Status>")
                .to(String.format("activemq:queue:%s", bkQueueName));
    }
}

package etpmv.code;

import org.apache.camel.CamelContext;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.impl.CompositeRegistry;
import org.apache.camel.impl.DefaultCamelContext;
import org.apache.camel.impl.SimpleRegistry;
import org.apache.camel.main.Main;
import org.apache.camel.management.JmxNotificationEventNotifier;
import org.apache.camel.spring.SpringCamelContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.util.Collection;

public abstract class TalendRouteBuilder extends RouteBuilder {

    @Override
    public void configure() {
        doConfig();
//        configRegistry(registry);
    }

    public void doConfig() {
//        final CamelContext camelContext = getContext();
//
//        CompositeRegistry compositeRegistry = new CompositeRegistry();
//        compositeRegistry.addRegistry(camelContext.getRegistry());
//        compositeRegistry.addRegistry(new SimpleRegistry());
//        ((DefaultCamelContext) camelContext).setRegistry(compositeRegistry);
    }

    public void run() throws Exception {
        Main main = new Main() {
            protected CamelContext createContext() {
                final DefaultCamelContext camelContext = new SpringCamelContext(new
                        ClassPathXmlApplicationContext("META-INF/spring/application.xml"));
                camelContext.setName(this.getClass().getName());
                Collection<JmxNotificationEventNotifier> jmxEventNotifiers = camelContext
                        .getRegistry()
                        .findByType(
                                JmxNotificationEventNotifier.class);
                if (jmxEventNotifiers != null && !jmxEventNotifiers.isEmpty()) {
                    camelContext.getManagementStrategy().addEventNotifier(
                            jmxEventNotifiers.iterator().next());
                }
                return camelContext;
            }
        };

        main.addRouteBuilder(this);

        main.run();
    }

}

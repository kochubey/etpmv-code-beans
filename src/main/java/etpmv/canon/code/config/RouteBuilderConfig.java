package etpmv.canon.code.config;

import org.apache.camel.CamelContext;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.impl.CompositeRegistry;
import org.apache.camel.impl.DefaultCamelContext;
import org.apache.camel.impl.SimpleRegistry;

public abstract class RouteBuilderConfig extends RouteBuilder {

    @Override
    public void configure() {
        doConfig();
    }

    private void doConfig() {
        final CamelContext camelContext = getContext();

        final SimpleRegistry registry = new SimpleRegistry();
        CompositeRegistry compositeRegistry = new CompositeRegistry();
        compositeRegistry.addRegistry(camelContext.getRegistry());
        compositeRegistry.addRegistry(registry);
        ((DefaultCamelContext) camelContext).setRegistry(compositeRegistry);

        configRegistry(registry);
        initRoute();
    }

    protected abstract void initRoute();

    protected abstract void configRegistry(SimpleRegistry registry);



}

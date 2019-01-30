package etpmv.system.impl;

import org.apache.camel.builder.RouteBuilder;
import routines.system.api.TalendESBRoute;

public class TalendESBRouteImpl extends RouteBuilder implements TalendESBRoute {
    private final ContextProperties config;

    public TalendESBRouteImpl() {
        config = new ContextProperties();
    }

    public ContextProperties getConfig() {
        return config;
    }

    public void setArguments(Object arguments) {
        config.setArguments(arguments);
    }

    @Override
    public void stop() throws Exception {
    }

    @Override
    public void shutdown() throws Exception {
    }

    @Override
    public String[][] runJob(String[] args) {
        int exitCode = runJobInTOS(args);
        return new String[][]{{Integer.toString(exitCode)}};
    }

    @Override
    public int runJobInTOS(String[] args) {
        return 0;
    }

    @Override
    public void configure() throws Exception {
    }

    public void stop(org.apache.camel.main.Main main) throws java.lang.Exception {
        if (main != null) {
            main.stop();
        }
    }

    public void shutdown(org.apache.camel.main.Main main) throws java.lang.Exception {
        if (main != null) {
            main.shutdown();
        }
    }

}

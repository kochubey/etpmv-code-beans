package etpmv.canon.code;

import org.apache.camel.CamelContext;
import org.apache.camel.component.validator.ValidatorResourceResolverFactory;
import org.w3c.dom.ls.LSResourceResolver;

public class ResourceResolverFactoryBean implements ValidatorResourceResolverFactory {

    @Override
    public LSResourceResolver createResourceResolver(CamelContext camelContext, String rootResourceUri) {
        return new WikiResourceResolver(camelContext, rootResourceUri);
    }
}

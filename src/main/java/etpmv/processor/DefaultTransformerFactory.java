package etpmv.processor;

import javax.xml.XMLConstants;
import javax.xml.transform.TransformerConfigurationException;

public class DefaultTransformerFactory extends org.apache.xalan.processor.TransformerFactoryImpl {
    public DefaultTransformerFactory() throws TransformerConfigurationException {
        super();
        this.setFeature(XMLConstants.FEATURE_SECURE_PROCESSING, false);
    }
}

package etpmv.system;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;

@XmlRegistry
public class ObjectFactory {

    private final static QName _Status_QNAME = new QName("urn://dts/shod/exchange/v1_1", "Status");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: rk_esb_connectors.sedo_facade_0_1.model.esb
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link StatusType }
     * 
     */
    public StatusType createStatusType() {
        return new StatusType();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link StatusType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn://dts/shod/exchange/v1_1", name = "Status")
    public JAXBElement<StatusType> createStatus(StatusType value) {
        return new JAXBElement<>(_Status_QNAME, StatusType.class, null, value);
    }

}

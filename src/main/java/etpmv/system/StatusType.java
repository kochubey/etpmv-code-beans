
package etpmv.system;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import java.math.BigInteger;


/**
 * <p>Java class for StatusType complex type.
 *
 * <p>The following schema fragment specifies the expected content contained within this class.
 *
 * <pre>
 * &lt;complexType name="StatusType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="Code" type="{http://www.w3.org/2001/XMLSchema}integer"/>
 *         &lt;element name="Result" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="Description" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "StatusType", namespace = "urn://dts/shod/exchange/v1_1", propOrder = {
        "code",
        "result",
        "description"
})
public class StatusType {

    public StatusType() {
    }

    public StatusType(Integer code, String result, String description) {
        this.code = code;
        this.result = result;
        this.description = description;
    }

    @XmlElement(name = "Code", namespace = "urn://dts/shod/exchange/v1_1", required = true)
    protected Integer code;
    @XmlElement(name = "Result", namespace = "urn://dts/shod/exchange/v1_1", required = true)
    protected String result;
    @XmlElement(name = "Description", namespace = "urn://dts/shod/exchange/v1_1", required = true)
    protected String description;

    /**
     * Gets the value of the code property.
     *
     * @return possible object is
     * {@link BigInteger }
     */
    public Integer getCode() {
        return code;
    }

    /**
     * Sets the value of the code property.
     *
     * @param value allowed object is
     *              {@link BigInteger }
     */
    public void setCode(Integer value) {
        this.code = value;
    }

    /**
     * Gets the value of the result property.
     *
     * @return possible object is
     * {@link String }
     */
    public String getResult() {
        return result;
    }

    /**
     * Sets the value of the result property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setResult(String value) {
        this.result = value;
    }

    /**
     * Gets the value of the description property.
     *
     * @return possible object is
     * {@link String }
     */
    public String getDescription() {
        return description;
    }

    /**
     * Sets the value of the description property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setDescription(String value) {
        this.description = value;
    }

}

package etpmv.system;

import etpmv.canon.code.processors.UrlProcessor;
import org.apache.camel.Exchange;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

import static etpmv.system.Headers.*;
import static java.lang.String.format;
import static java.net.HttpURLConnection.HTTP_OK;
import static java.util.Optional.ofNullable;

public class Data {
    private String issuer;
    private String form;
    private String version;
    private String requester;
    private String responser;
    private List<String> subscribers;

    public Data(String issuer, String form, String version, String requester, String responser) {
        this.issuer = issuer;
        this.form = form;
        this.version = version;
        this.requester = requester;
        this.responser = responser;
    }

    public Data(Exchange exchange) {
        this(
                ofNullable(exchange.getIn().getHeader(DATA_SOURCE, String.class)).orElse("")
                        .replaceAll("^urn://dts/(.*)/(.*)/(.*)$", "$1"),
                ofNullable(exchange.getIn().getHeader(DATA_SOURCE, String.class)).orElse("")
                        .replaceAll("^urn://dts/(.*)/(.*)/(.*)$", "$2"),
                ofNullable(exchange.getIn().getHeader(DATA_SOURCE, String.class)).orElse("")
                        .replaceAll("^urn://dts/(.*)/(.*)/(.*)$", "$3"),
                ofNullable(exchange.getIn().getHeader(REQUEST_ID, String.class)).orElse("")
                        .replaceAll("^urn:pts:(.*):(.*)$", "$1"),
                ofNullable(exchange.getIn().getHeader(RESPONSE_ID, String.class)).orElse("")
                        .replaceAll("^urn:pts:(.*):(.*)$", "$1"));
    }

    public String getForm() {
        return form;
    }

    public String getIssuer() {
        return issuer;
    }

    public String getRequester() {
        return requester;
    }

    public String getResponser() {
        return responser;
    }

    public String getVersion() {
        return version;
    }

    public String path() {
        return String.format("/DSE/urn/pts/%s/dts/%s/%s/body.xsd", issuer, form, version);
    }

    public boolean isReleasedOn(String url) throws IOException {
        HttpURLConnection con = (HttpURLConnection) new URL(url + path() + "/body.xsd").openConnection();
        con.setRequestMethod("HEAD");
        int responseCode = con.getResponseCode();
        con.disconnect();
        return (responseCode == HTTP_OK);
    }

    public List<String> subscribersOn(String url) {
        return this.subscribers = new UrlProcessor().getListFromJsonByUrl(
                format("%s/api/subscribersList?ptsId=%s&dtsId=%s&version=%s",
                        url, issuer, form, version));
    }
}
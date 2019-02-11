package etpmv.system;

import etpmv.canon.code.exceptions.DuplicateTransactionException;
import etpmv.canon.code.exceptions.TransactionTimeoutException;
import etpmv.canon.code.processors.UrlProcessor;
import org.apache.camel.Exchange;
import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

import static java.lang.String.format;
import static java.net.HttpURLConnection.HTTP_CONFLICT;
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
                ofNullable(exchange.getIn().getHeader("X-Data-Source", String.class)).orElse("")
                        .replaceAll("^urn://dts/(.*)/(.*)/(.*)$", "$1"),
                ofNullable(exchange.getIn().getHeader("X-Data-Source", String.class)).orElse("")
                        .replaceAll("^urn://dts/(.*)/(.*)/(.*)$", "$2"),
                ofNullable(exchange.getIn().getHeader("X-Data-Source", String.class)).orElse("")
                        .replaceAll("^urn://dts/(.*)/(.*)/(.*)$", "$3"),
                ofNullable(exchange.getIn().getHeader("X-Request-Id", String.class)).orElse("")
                        .replaceAll("^urn:pts:(.*):(.*)$", "$1"),
                ofNullable(exchange.getIn().getHeader("X-Response-Id", String.class)).orElse("")
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
        HttpURLConnection con = (HttpURLConnection) new URL(url + path()).openConnection();
        con.setRequestMethod("HEAD");
        int responseCode = con.getResponseCode();
        con.disconnect();
        return (responseCode == HTTP_OK);
    }

    public List<String> subscribersOn(String url) {
        return this.subscribers = UrlProcessor.getInstance().getListFromJsonByUrl(
                format("%s/api/subscribersList?ptsId=%s&dtsId=%s&version=%s",
                        url, issuer, form, version));
    }
}
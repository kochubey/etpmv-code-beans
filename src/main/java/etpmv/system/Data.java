package etpmv.system;

import etpmv.canon.code.processors.UrlProcessor;
import org.apache.camel.Exchange;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import static java.lang.String.format;
import static java.net.HttpURLConnection.HTTP_OK;
import static java.util.Optional.ofNullable;

public class Data {
    private String issuer;
    private String form;
    private String version;
    private String requester;
    private String responser;
    private String subscriber;
    private List<String> subscribers;

    public Data(Exchange exchange) {
        this.issuer = ofNullable(exchange.getIn().getHeader("X-Data-Source", String.class)).orElse("")
                .replaceAll("^urn://dts/(.*)/(.*)/(.*)$", "$1");
        this.form = ofNullable(exchange.getIn().getHeader("X-Data-Source", String.class)).orElse("")
                .replaceAll("^urn://dts/(.*)/(.*)/(.*)$", "$2");
        this.version = ofNullable(exchange.getIn().getHeader("X-Data-Source", String.class)).orElse("")
                .replaceAll("^urn://dts/(.*)/(.*)/(.*)$", "$3");
        this.requester = ofNullable(exchange.getIn().getHeader("X-Request-Id", String.class)).orElse("")
                .replaceAll("^urn:pts:(.*):(.*)$", "$1");
        this.responser = ofNullable(exchange.getIn().getHeader("X-Response-Id", String.class)).orElse("")
                .replaceAll("^urn:pts:(.*):(.*)$", "$1");
        this.subscribers = new ArrayList<>();
    }

    private String $(String format, Object... args) {
        return format(format, args);
    }

    public String form() {
        return form;
    }

    public String issuer() {
        return issuer;
    }

    public String requester() {
        return requester;
    }

    public String responser() {
        return responser;
    }

    public String version() {
        return version;
    }

    public String subscriber() {
        return subscriber;
    }

    public String path() {
        return $("/DSE/urn/pts/%s/dts/%s/%s/", issuer, form, version);
    }

    public String pathOn(String first) {
        return $("%s/%s", first, path());
    }

    @Deprecated
    public List<String> subscribersOn(String url) {
        return this.subscribers = (subscribers.size() == 0) ? new UrlProcessor().getListFromJsonByUrl(
                $("%s/api/subscribersList?ptsId=%s&dtsId=%s&version=%s",
                        url, issuer, form, version)) : subscribers;
    }

    public boolean isAuthorizedOn(String pts, String url) {
        List<String> subs = new ArrayList<>();
        subs.addAll(this.subscribersOn(url));
        subs.add("shod");
        subs.add(issuer);
        return subs.contains(String.valueOf(pts));
    }

    public boolean isBroadcast() {
        return !requester.isEmpty() && responser.isEmpty() && issuer.equals(requester); //&& subscribersOn(url).size() > 0;
    }

    public boolean isAck() {
        return !requester.isEmpty() && !responser.isEmpty() && issuer.equals(requester);
    }

    public boolean isRequest() {
        return !requester.isEmpty() && responser.isEmpty() && !issuer.equals(requester);
    }

    public boolean isResponse() {
        return !requester.isEmpty() && !responser.isEmpty() && !issuer.equals(requester);
    }

    public boolean isReleasedOn(String url) throws IOException {
        HttpURLConnection con = (HttpURLConnection) new URL(pathOn(url) + "/body.xsd").openConnection();
        con.setRequestMethod("HEAD");
        int responseCode = con.getResponseCode();
        con.disconnect();
        return (responseCode == HTTP_OK);
    }

    public String unmarshal(String webConfigUrl, String requestId, String endpointPtsKey) {
        boolean isResponseFromSource =  !issuer.equals(requester) && !responser.isEmpty();
        boolean isBroadcastRequest = issuer.equals(requester) && responser.isEmpty();
        boolean isRequestToSubFromShod = requestId.startsWith("urn:pts:shod:sub-") && responser.isEmpty();

        String xsltFileName = (isBroadcastRequest || isResponseFromSource || isRequestToSubFromShod)  ? "vs.sub.xslt" : null;
        if (xsltFileName==null) return null;

        // определение ПТС, у которой брать xslt
        String subscriberPtsId = (isBroadcastRequest || isRequestToSubFromShod) ? endpointPtsKey : requester;

        return xsltUrl(webConfigUrl, subscriberPtsId, xsltFileName);
    }

    @Deprecated
    public String getForm() {
        return form;
    }

    @Deprecated
    public String getIssuer() {
        return issuer;
    }

    @Deprecated
    public String getRequester() {
        return requester;
    }

    @Deprecated
    public String getResponser() {
        return responser;
    }

    @Deprecated
    public String getVersion() {
        return version;
    }

    public String xsltUrlForMarshal(String webConfigUrl) {
        boolean isRequestToSource = issuer.equals(requester) && responser.isEmpty();
        boolean isBroadcastResponse = issuer.equals(requester) && !responser.isEmpty();

        String xsltFileName = (isRequestToSource || isBroadcastResponse) ? "sub.vs.xslt" : null;
        if (xsltFileName==null) return null;

        // определение ПТС, у которой брать xslt
        String subscriberPtsId = isRequestToSource ? requester : responser;
        return xsltUrl(webConfigUrl, subscriberPtsId, xsltFileName);
    }

    @Deprecated
    public String xsltUrlForUnmarshal(String webConfigUrl, String requestId, String endpointPtsKey) {
        boolean isResponseFromSource =  !issuer.equals(requester) && !responser.isEmpty();
        boolean isBroadcastRequest = issuer.equals(requester) && responser.isEmpty();
        boolean isRequestToSubFromShod = requestId.startsWith("urn:pts:shod:sub-") && responser.isEmpty();

        String xsltFileName = (isBroadcastRequest || isResponseFromSource || isRequestToSubFromShod)  ? "vs.sub.xslt" : null;
       if (xsltFileName==null) return null;

        // определение ПТС, у которой брать xslt
        String subscriberPtsId = (isBroadcastRequest || isRequestToSubFromShod) ? endpointPtsKey : requester;

        return xsltUrl(webConfigUrl, subscriberPtsId, xsltFileName);
    }

    private String xsltUrl(String webConfigUrl, String subscriberPtsId, String xsltFileName) {
        return $("%s%s/dts/%s/%s/subscribers/%s/%s", webConfigUrl, issuer, form, version, subscriberPtsId, xsltFileName);
    }
}
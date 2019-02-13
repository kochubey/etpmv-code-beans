package etpmv.canon.code.processors;

import etpmv.system.Data;
import org.apache.commons.io.IOUtils;
import com.google.gson.Gson;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import static java.lang.String.format;


// todo нужно избавляться от datasourceUrl в методах
// example http://localhost:8080/DSE/urn/pts/uzd44/dts/contract/v1.0.1/subscribers/pcp/endpoint
public class UrlProcessor {
    private final String regex = "^(.*)/DSE/urn/pts/(.*)/dts/(.*)/(.*)/subscribers/(.*)/endpoint";
    private final String iss_point = "%s/DSE/urn/pts/%s/endpoint";
    private final String sub_point = "%s/DSE/urn/pts/%s/dts/%s/%s/subscribers/%s/endpoint";

    private String host;
    private String issuer;
    private String form;
    private String version;
    private String subscriber;

    public UrlProcessor(String host, String issuer, String form, String version, String subscriber) {
        this.host = host;
        this.issuer = issuer;
        this.form = form;
        this.version = version;
        this.subscriber = subscriber;
    }

    public UrlProcessor(String host, Data data) {
        this(host, data.issuer(), data.form(), data.version(), data.requester());
    }

    public UrlProcessor(String host, Data data, String subscriber) {
        this(host, data.issuer(), data.form(), data.version(), subscriber);
    }

    public UrlProcessor(String url) {
        this.host = url.replaceAll(regex, "$1");
        this.issuer = url.replaceAll(regex, "$2");
        this.form = url.replaceAll(regex, "$3");
        this.version = url.replaceAll(regex, "$4");
        this.subscriber = url.replaceAll(regex, "$5");
    }

    @Deprecated
    public UrlProcessor() {
        this("127.0.0.1:9090");
    }

    private String $(String format, Object... args) {
        return format(format, args);
    }

    private String url$(String url) {
        try {
            return IOUtils.toString(new URL(url), "UTF-8");
        } catch (IOException e) {
            return "";
        }
    }

    public String getIssuerPath() throws MalformedURLException {
        return $(iss_point, host, issuer);
    }

    public String getIssuerEndpoint() throws MalformedURLException {
        return url$(getIssuerPath());
    }

    public String getSubscriberPath() throws MalformedURLException {
        return $(iss_point, host, issuer, form, version, subscriber);
    }

    public String getSubscriberEndpoint() throws MalformedURLException {
        return url$(getSubscriberPath());
    }

    @Deprecated
    public String getSubscriberEndpoint(String datasourceUrl, String subscriberPtsId, String subscriberUrlPart) throws MalformedURLException {
        String subscribersUrl = format("%s%s", datasourceUrl, subscriberUrlPart);
        URL url = new URL(format("%s/%s%s", subscribersUrl, subscriberPtsId, subscriberUrlPart));
        try {
            return IOUtils.toString(url, "UTF-8");
        } catch (IOException e) {
            return "";
        }
    }

    @Deprecated
    public String getIssuerEndpoint(String datasourceUrl) throws MalformedURLException {
        try {
            return IOUtils.toString(new URL(format("%s/endpoint", datasourceUrl)), "UTF-8");
        } catch (IOException e) {
            return "";
        }
    }

    @Deprecated
    public String getSubscribersUrl(String datasourceUrl, String subscriberUrlPart) {
        return format("%s%s", datasourceUrl, subscriberUrlPart);
    }

    public List<String> getListFromJsonByUrl(String url) {
        URL urlObj;
        List<String> listFromJson = new ArrayList<>(0);
        try {
            urlObj = new URL(url);
        } catch (MalformedURLException e) {
            return listFromJson;
        }
        try {
            listFromJson = new Gson().fromJson(IOUtils.toString(urlObj, "UTF-8"), List.class);
//=======
//        try{
//            listFromJson =  new Gson().<List<String>>fromJson(IOUtils.toString(urlObj, "UTF-8"), List.class);
//>>>>>>> cb0386c1cf0efba4bca752104a516d52b71f46b6
        } catch (IOException e) {
            return listFromJson;
        }
        return listFromJson;
    }
}

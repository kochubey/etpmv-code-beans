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
    private final static String regex = "^(.*)/DSE/urn/pts/(.*)/dts/(.*)/(.*)/subscribers/(.*)/endpoint";
    private final static String iss_point = "%s/DSE/urn/pts/%s/endpoint";
    private final static String sub_point = "%s/DSE/urn/pts/%s/dts/%s/%s/subscribers/%s/endpoint";
    private final static String all_subs = "%s/api/subscribersList?ptsId=%s&dtsId=%s&version=%s";

    private String host;
    private String issuer;
    private String form;
    private String version;
    private String subscriber;
    private List<String> subscribers;

//    public static void main(String[] args) {
//        UrlProcessor processor = new UrlProcessor("http://localhost:8080/");
//        System.out.println(processor.getIssuerPath());
//        System.out.println(processor.getIssuerEndpoint());
//    }

    public UrlProcessor(String host, String issuer, String form, String version, String subscriber) {
        this.host = host;
        this.issuer = issuer;
        this.form = form;
        this.version = version;
        this.subscriber = subscriber;
        this.subscribers = new ArrayList<>();
    }

    public UrlProcessor(String host, Data data) {
        this(host, data.issuer(), data.form(), data.version(), data.requester());
    }

    public UrlProcessor(String host, Data data, String subscriber) {
        this(host, data.issuer(), data.form(), data.version(), subscriber);
    }

    public UrlProcessor(String url) {
        this.host = url.replaceAll(regex, "$1");
        this.issuer = url.replaceAll(regex, "$2").replace(url, "");
        this.form = url.replaceAll(regex, "$3").replace(url, "");
        this.version = url.replaceAll(regex, "$4").replace(url, "");
        this.subscriber = url.replaceAll(regex, "$5").replace(url, "");
        this.subscribers = new ArrayList<>();
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

    public List<String> subscribers() {
        return this.subscribers = (subscribers.size() > 0) ? subscribers :
                getListFromJsonByUrl($(all_subs, host, issuer, form, version));
    }

    public String getIssuerPath() {
        return $(iss_point, host, issuer + "/dts/" + form + "/" + version);
    }

    public String getIssuerEndpoint() {
        return url$(getIssuerPath());
    }

    public String getSubscriberPath() {
        return $(sub_point, host, issuer, form, version, subscriber);
    }

    public String getSubscriberEndpoint() {
        return url$(getSubscriberPath());
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
            listFromJson = new Gson().<List<String>>fromJson(IOUtils.toString(urlObj, "UTF-8"), List.class);
        } catch (IOException e) {
            return listFromJson;
        }
        return listFromJson;
    }
}

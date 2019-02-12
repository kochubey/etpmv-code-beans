package etpmv.canon.code.processors;

import org.apache.commons.io.IOUtils;
import com.google.gson.Gson;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import static java.lang.String.format;


public class UrlProcessor {

    public UrlProcessor() {}
    
    public String getEndpointUrlForSubscriber(String datasourceUrl, String subscriberPtsId, String subscriberUrlPart) throws MalformedURLException {
        String subscribersUrl = format("%s%s", datasourceUrl, subscriberUrlPart);
        URL url = new URL(format("%s/%s%s", subscribersUrl, subscriberPtsId, subscriberUrlPart));
        try {
            return IOUtils.toString(url, "UTF-8");
        } catch (IOException e) {
            return "";
        }
    }
    
    public String getSubscribersUrl(String datasourceUrl, String subscriberUrlPart) {
        return format("%s%s", datasourceUrl, subscriberUrlPart);
    }

    public List<String> getListFromJsonByUrl(String url) {
        URL urlObj;
        List<String> listFromJson = new ArrayList<>(0);
        try {
            urlObj = new URL(url);
        }catch (MalformedURLException e) {
            return listFromJson;
        }
        try{
            listFromJson =  new Gson().<List<String>>fromJson(IOUtils.toString(urlObj, "UTF-8"), List.class);
        } catch (IOException e) {
           return listFromJson;
        }
        return listFromJson;
    }
}

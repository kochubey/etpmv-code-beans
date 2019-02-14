package etpmv.system;

import com.google.gson.Gson;
import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.lang.String.format;
import static java.util.Arrays.asList;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Stream.*;

public class Transaction {
//    private final String DATA_SUBSCRIBERS_PATH = "%s/api/subscribersList?ptsId=%s&dtsId=%s&version=%s";

    private Config config;
    private Data data;
    private List<String> subscribers = new ArrayList<>();

    public Transaction(Config config, Data data) {
        this.config = config;
        this.data = data;
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

    public Config config() {
        return config;
    }

    public Data data() {
        return data;
    }


    public List<String> authorized() {
        return this.subscribers
                = (subscribers.isEmpty()) ?
                concat(
                        asList(data.issuer(), config.owher()).stream(),
                        toList(config.url() + data.path() + "/subscribers").stream()
                ).collect(Collectors.toList())
                : subscribers;
    }

    public List<String> toList(String url) {
        URL urlObj;
        List<String> listFromJson = new ArrayList<>(0);
        try {
            urlObj = new URL(url);
        } catch (MalformedURLException e) {
            return listFromJson;
        }
        try {
            listFromJson = new Gson().fromJson(IOUtils.toString(urlObj, "UTF-8"), List.class);
        } catch (IOException e) {
            return listFromJson;
        }
        return listFromJson;
    }
}

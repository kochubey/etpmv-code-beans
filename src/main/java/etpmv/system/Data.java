package etpmv.system;

import etpmv.canon.code.processors.UrlProcessor;
import org.apache.camel.Exchange;
import org.apache.camel.Message;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import static etpmv.system.Headers.*;
import static java.lang.String.format;
import static java.net.HttpURLConnection.HTTP_OK;
import static java.util.Optional.ofNullable;

public class Data {
    private String requestId;
    private String responseId;
    private String dataSource;
    private String exchangeId;
    private String targetPts;
    private Integer seqNum;
    private Boolean isLast;

    private String issuer;
    private String form;
    private String version;
    private String requester;
    private String responser;
    private String subscriber;
    private List<String> subscribers;

    private static final String URN_PTS = "^urn:pts:(.*):(.*)$";
    private static final String URN_DTS = "^urn://dts/(.*)/(.*)/(.*)$";
    private static final String SHOD = "shod";

    public Data(Exchange exchange) {
        this(exchange.getIn());
    }

    public Data(Message message) {
        this.dataSource = message.getHeader(DATA_SOURCE, String.class);
        this.requestId = message.getHeader(REQUEST_ID, String.class);
        this.responseId = ofNullable(message.getHeader(RESPONSE_ID, String.class)).orElse("");
        this.exchangeId = message.getHeader(EXCHANGE_ID, String.class);
        this.seqNum =  ofNullable(message.getHeader(SEQ_NUM, Integer.class)).orElse(1);
        this.isLast =  ofNullable(message.getHeader(IS_LAST, Boolean.class)).orElse(true);
        this.targetPts = ofNullable(message.getHeader(TARGET_PTS, String.class)).orElse("");

        this.issuer = ofNullable(message.getHeader(DATA_SOURCE, String.class)).orElse("")
                .replaceAll(URN_DTS, "$1");
        this.form = ofNullable(message.getHeader(DATA_SOURCE, String.class)).orElse("")
                .replaceAll(URN_DTS, "$2");
        this.version = ofNullable(message.getHeader(DATA_SOURCE, String.class)).orElse("")
                .replaceAll(URN_DTS, "$3");
        this.requester = ofNullable(message.getHeader(REQUEST_ID, String.class)).orElse("")
                .replaceAll(URN_PTS, "$1");
        this.responser = ofNullable(message.getHeader(RESPONSE_ID, String.class)).orElse("")
                .replaceAll(URN_PTS, "$1");
        this.subscriber = computeSubscriber();
        this.subscribers = new ArrayList<>();
    }

    private String computeSubscriber() {
        //Если это ответное сообщение
        if (!responser.isEmpty()) {
            return subscriberForResponse();
            //Это запрос
        } else {
            return subscriberForRequest();
        }
    }

    private String subscriberForResponse() {
        //Это ответ на рассылку или на запрос от ШОД
        if (issuer.equals(requester) || requester.equals(SHOD)) {
            return responser;
        } else {
            //Это ответ от источника подписанту
            return requester;
        }
    }

    private String subscriberForRequest() {
        //Запрос от ШОД
        if (requester.equals(SHOD)) {
            //Запрос от ШОД к источнику ВС
            if (requestId.startsWith(REQUEST_ID_SRC)) {
                return issuer;
                //Запрос от ШОД подписанту
            } else {
                return targetPts;
            }
            //Запрос от ПТС
        } else {
            //Рассылка от источника
            if (requester.equals(issuer)) {
                return targetPts;
                //Запрос от подписанта к источнику
            } else {
                return requester;
            }
        }
    }

    private String $(String format, Object... args) {
        return format(format, args);
    }

    public String requestId() { return requestId;}

    public String responseId() { return responseId;}

    public String dataSource() { return dataSource;}

    public String exchangeId() { return exchangeId;}

    public String targetPts() { return targetPts;}

    public Integer seqNum() { return seqNum;}

    public Boolean isLast() { return isLast;}

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
        return $("%s%s", first, path());
    }

    public boolean isAuthorizedOn(String pts, String url) {
        List<String> subs = new ArrayList<>(new UrlProcessor(url, this).subscribers());
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
        HttpURLConnection con = (HttpURLConnection) new URL(pathOn(url) + "body.xsd").openConnection();
        con.setRequestMethod("GET");
        int responseCode = con.getResponseCode();
        con.disconnect();
        return (responseCode == HTTP_OK);
    }
}
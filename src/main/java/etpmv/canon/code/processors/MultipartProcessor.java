package etpmv.canon.code.processors;

import org.apache.camel.Exchange;
import org.apache.http.HttpEntity;
import org.apache.http.entity.BufferedHttpEntity;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.FileBody;

import java.io.File;
import java.io.IOException;

public class MultipartProcessor {

    private static MultipartProcessor multipartProcessor;

    private MultipartProcessor() {}

    public static MultipartProcessor getInstance() {
        if (multipartProcessor==null) {
            multipartProcessor = new MultipartProcessor();
        }
        return multipartProcessor;
    }

    public void convertBodyToMultipart(Exchange exchange, boolean withFile) throws IOException {
        String body = exchange.getIn().getBody(String.class);

        MultipartEntityBuilder multipartEntityBuilder = MultipartEntityBuilder.create();
        ContentType contentType;

        if (withFile) {
            String filePath = String.format("%1$s%2$s", "./data/tmp/files/", exchange.getIn().getHeader("EsbExchangeId",	String.class));
            File file = new File(filePath);
            if (file.isFile()) {
                contentType = ContentType.create(ContentType.APPLICATION_OCTET_STREAM.getMimeType(),"UTF-8");
                multipartEntityBuilder.addPart("AttachedField",	new FileBody(file, contentType, file.getName()));
            }
        }

        contentType = ContentType.create(exchange.getIn().getHeader("X-Message-Type", String.class),"UTF-8");
        multipartEntityBuilder.addTextBody("MessageText", body, contentType);

        HttpEntity resultEntity = new BufferedHttpEntity(multipartEntityBuilder.build());
        exchange.getIn().setHeader("Content-Type", resultEntity.getContentType().getValue());
        exchange.getIn().setBody(resultEntity);
    }
}

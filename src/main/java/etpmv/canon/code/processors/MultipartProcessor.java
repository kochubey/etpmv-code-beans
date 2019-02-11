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

    public MultipartProcessor() {}

    public void marshal(Exchange exchange) throws IOException {
        this.marshal(exchange, null, null);
    }

    public void marshal(Exchange exchange, String exchangeId, String fileName) throws IOException {
        String body = exchange.getIn().getBody(String.class);

        MultipartEntityBuilder multipartEntityBuilder = MultipartEntityBuilder.create();
        ContentType contentType;

        if (exchangeId!=null) {
            String filePath = String.format("%1$s%2$s", "./data/tmp/files/", exchangeId);
            File file = new File(filePath);
            if (fileName==null) fileName = file.getName();
            if (file.isFile()) {
                contentType = ContentType.create(ContentType.APPLICATION_OCTET_STREAM.getMimeType(),"UTF-8");
                multipartEntityBuilder.addPart("AttachedField",	new FileBody(file, contentType, fileName));
            }
        }

        contentType = ContentType.create(exchange.getIn().getHeader("X-Message-Type", String.class),"UTF-8");
        multipartEntityBuilder.addTextBody("MessageText", body, contentType);

        HttpEntity resultEntity = new BufferedHttpEntity(multipartEntityBuilder.build());
        exchange.getIn().setHeader("Content-Type", resultEntity.getContentType().getValue());
        exchange.getIn().setBody(resultEntity);
    }
}

package etpmv.canon.code.processors;

import org.apache.camel.Exchange;
import org.apache.camel.Message;
import org.apache.http.HttpEntity;
import org.apache.http.entity.BufferedHttpEntity;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.FileBody;

import java.io.File;
import java.io.IOException;

import static java.lang.String.format;
import static java.util.Optional.ofNullable;
import static org.apache.http.entity.ContentType.create;

public class MultipartProcessor {

    // todo marshal должен возвращать объект
    public void marshal(Exchange exchange) throws IOException {
        Message in = exchange.getIn();
        String body = in.getBody(String.class);
        String xExchangeId = in.getHeader("X-Exchange-Id", String.class);
        String xAttachName = in.getHeader("X-Attach-Name", String.class);
        String xAttachType = in.getHeader("X-Attach-Type", String.class);
        String xMessageType = in.getHeader("X-Message-Type", String.class);

        MultipartEntityBuilder builder = MultipartEntityBuilder.create()
                .addTextBody("MessageText", body, create(xMessageType, "UTF-8"));

        if (xExchangeId != null) {
            File file = new File(format("%1$s%2$s", "./data/tmp/files/", xExchangeId));
            if (file.isFile()) {
                builder.addPart("AttachedField", new FileBody(file
                        , create(xAttachType, "UTF-8")
                        , ofNullable(xAttachName).orElse(file.getName()))
                );
            }
        }

        HttpEntity resultEntity = new BufferedHttpEntity(builder.build());
        in.setHeader("Content-Type", resultEntity.getContentType().getValue());
        in.setBody(resultEntity);
    }
}

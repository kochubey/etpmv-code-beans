package etpmv.canon.code.processors;

import etpmv.system.Data;
import org.apache.camel.CamelContext;
import org.apache.camel.Exchange;
import org.apache.camel.Message;
import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.http.HttpEntity;
import org.apache.http.entity.BufferedHttpEntity;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.InputStream;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import static java.lang.String.format;
import static java.util.Optional.ofNullable;
import static org.apache.http.entity.ContentType.create;

public class MultipartProcessor {

    // todo marshal должен возвращать объект
    public void marshal(Exchange exchange) throws Exception {
        Message in = exchange.getIn();
        String body = in.getBody(String.class);
        String xExchangeId = in.getHeader("X-Exchange-Id", String.class);
        String xAttachName = in.getHeader("X-Attach-Name", String.class);
        String xAttachType = in.getHeader("X-Attach-Type", String.class);
        String xMessageType = in.getHeader("X-Message-Type", String.class);

        MultipartEntityBuilder builder = MultipartEntityBuilder.create()
                .addTextBody("MessageText", body, create(xMessageType, "UTF-8"))
                .setMode(HttpMultipartMode.RFC6532);

        if (in.getHeader("X-On-FTP") != null) {
            receiveFromFtp(exchange, builder);
        }else if (xExchangeId != null) {
            File file = new File(format("%1$s%2$s", "./data/tmp/files/", xExchangeId));
            if (file.isFile()) {
                xAttachName = URLDecoder.decode(xAttachName, "UTF-8");
                System.out.println("MultipartProcessor incoming Content-Type: " + xAttachType);
                if(xAttachType==null || xAttachType.equals(""))
                    xAttachType="application/octet-stream";
                else if(xAttachType.contains(";"))
                    xAttachType=xAttachType.split(";")[0];
                builder.addBinaryBody("AttachedField", file, create(xAttachType, "UTF-8"),
                        ofNullable(xAttachName).orElse(file.getName()));
                System.out.println("MultipartProcessor outgoing Content-Type: " + xAttachType);
            }
        }

        HttpEntity resultEntity = new BufferedHttpEntity(builder.build());
        in.setHeader("Content-Type", resultEntity.getContentType().getValue());
        in.setBody(resultEntity);
    }

    private void receiveFromFtp(Exchange exchange, MultipartEntityBuilder builder) throws Exception {
        Data data = new Data(exchange);
        CamelContext context = exchange.getContext();
        FTPClient ftpClient = new FTPClient();
        ftpClient.connect(context.resolvePropertyPlaceholders("{{etpmv.ftp.url}}"), new Integer(context.resolvePropertyPlaceholders("{{etpmv.ftp.port}}")));
        ftpClient.login(context.resolvePropertyPlaceholders("{{etpmv.ftp.username}}"), context.resolvePropertyPlaceholders("{{etpmv.ftp.password}}"));
        ftpClient.enterLocalPassiveMode();
        ftpClient.setFileType(FTP.BINARY_FILE_TYPE);

        byte[] buffer = new byte[8192];
        try (ByteArrayOutputStream baos = new ByteArrayOutputStream();
             ZipOutputStream zos = new ZipOutputStream(baos)) {

            if (ftpClient.changeWorkingDirectory(data.requestUuid())) {
                for (FTPFile file : ftpClient.listFiles()) {
                    if (file.isFile()) {
                        ZipEntry zipEntry = new ZipEntry(file.getName());
                        zipEntry.setSize(file.getSize());
                        zos.putNextEntry(zipEntry);
                        try (InputStream fis = ftpClient.retrieveFileStream(file.getName())) {

                            int length;
                            while ((length = fis.read(buffer)) > 0) {
                                zos.write(buffer, 0, length);
                            }

                            ftpClient.completePendingCommand();
                            zos.closeEntry();
                        }
                    }
                }
            }
            zos.close();

            builder.addBinaryBody("AttachedField", baos.toByteArray(), ContentType.create("application/zip", StandardCharsets.UTF_8),
                    String.format("%s.zip", data.requestUuid()));
        }finally {
            if (ftpClient.isConnected()) {
                ftpClient.logout();
                ftpClient.disconnect();
            }
        }
    }
}

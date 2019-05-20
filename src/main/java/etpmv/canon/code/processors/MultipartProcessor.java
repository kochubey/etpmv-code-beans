package etpmv.canon.code.processors;

import etpmv.canon.code.exceptions.EtpmvException;
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
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.FileBody;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
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
                .addTextBody("MessageText", body, create(xMessageType, "UTF-8"));

        if (in.getHeader("X-On-FTP") != null) {
            retriveFromFtp(exchange, builder);
        }else if (xExchangeId != null) {
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

    private void retriveFromFtp(Exchange exchange, MultipartEntityBuilder builder) throws Exception {
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
                        try(InputStream fis = ftpClient.retrieveFileStream(file.getName())) {

                            int length;
                            while ((length = fis.read(buffer)) > 0) {
                                zos.write(buffer, 0, length);
                            }

                            ftpClient.completePendingCommand();
                            zos.closeEntry();
                        }catch (IOException e) {
                            throw new EtpmvException("1020", "Ошибка при получении файлов с FTP-сервера", e.getLocalizedMessage());
                        }
                    }
                }

                zos.close();

                builder.addBinaryBody("AttachedField", baos.toByteArray(), ContentType.create("application/zip", Charset.forName("UTF-8")),
                        String.format("%s.zip", data.requestUuid()));
            }
        } catch (IOException e) {
            throw new EtpmvException("1020", "Ошибка при получении файлов с FTP-сервера", e.getLocalizedMessage());
        }
    }
}

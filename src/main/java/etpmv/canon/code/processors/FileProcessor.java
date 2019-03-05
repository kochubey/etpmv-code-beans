package etpmv.routes.canon.utils;

import com.jayway.jsonpath.JsonPath;
import com.jayway.jsonpath.ReadContext;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;

import static java.lang.String.format;

public class FileResourceUtils {

    private final String filterFileName = "filter.xml";
    private final String datasourceFileLocation = "DSE/urn/pts/%s/dts/%s/%s";
    private final String datasourceVersionFileLocation = "DSE/urn/pts/%s/dts/%s";
    private final String subscribersFileLocation = "DSE/urn/pts/%s/dts/%s/%s/subscribers/%s";
    private final String ptsFileLocation = "DSE/urn/pts/%s";
    private final String webConfigFileLocation = "src/main/resources/DSEfiles";

    private String getFilePath(String ptsName, String dtsName, String dtsVersion, String subscriberName) {
        String filePath;

        if (ptsName != null) {
            if (dtsName != null) {
                if (dtsVersion != null) {
                    if (subscriberName != null) {
                        filePath = format(subscribersFileLocation, ptsName, dtsName, dtsVersion, subscriberName);
                    } else {
                        filePath = format(datasourceFileLocation, ptsName, dtsName, dtsVersion);
                    }
                } else {
                    filePath = format(datasourceVersionFileLocation, ptsName, dtsName);
                }
            } else {
                filePath = format(ptsFileLocation, ptsName);
            }
        } else {
            return null;
        }

        return filePath;
    }

    public String getFileContent(String fileName, String ptsName, String dtsName, String dtsVersion, String subscriberName) throws IOException {
        String filePath = getFilePath(ptsName, dtsName, dtsVersion, subscriberName);
        Path path;
        if (filePath == null) {
            path = Paths.get(webConfigFileLocation, fileName);
        } else {
            path = Paths.get(webConfigFileLocation, filePath, fileName);
        }
        return new String(Files.readAllBytes(path));
    }

    public String getFileContent(String fileName, String ptsName, String dtsName, String dtsVersion) throws IOException {
        return this.getFileContent(fileName, ptsName, dtsName, dtsVersion, null);
    }

    public String getFileContent(String fileName, String ptsName) throws IOException {
        return this.getFileContent(fileName, ptsName, null, null, null);
    }

    public String getFileContent(String fileName) throws IOException {
        return this.getFileContent(fileName, null, null, null, null);
    }

    public ArrayList<String> getList(String ptsName, String dtsName, String dtsVersion, String subscriber) {
        Path path = Paths.get(webConfigFileLocation, getFilePath(ptsName, dtsName, dtsVersion, subscriber));
        String[] pathArr = path.toFile().list();
        return (pathArr == null) ? new ArrayList<>(0) : new ArrayList<>(Arrays.asList(pathArr));
    }

    public ArrayList<String> getList(String ptsName, String dtsName, String dtsVersion) {
        return this.getList(ptsName, dtsName, dtsVersion, null);
    }

    public ArrayList<String> getList(String ptsName, String dtsName) {
        return this.getList(ptsName, dtsName, null);
    }

    public ArrayList<String> getList(String ptsName) {
        return this.getList(ptsName, null, null);
    }

    public String getEndpointUrlFromJson(String ptsName) throws IOException {
        return this.getEndpointUrlFromJson(ptsName, null, null, null);
    }

    public String getEndpointUrlFromJson(String ptsName, String dtsName, String dtsVersion) throws IOException {
        return this.getEndpointUrlFromJson(ptsName, dtsName, dtsVersion, null);
    }

    public String getEndpointUrlFromJson(String ptsName, String dtsName, String dtsVersion, String subscriberName) throws IOException {
        String fileContent = this.getFileContent("endpoint.json", ptsName, dtsName, dtsVersion, subscriberName);

        ReadContext ctx = JsonPath.parse(fileContent);
        String targetUrl = ctx.read("$.servers[0].url");
        LinkedHashMap targetPath = ctx.read("$.paths");
        return format("%s%s", targetUrl, targetPath.keySet().iterator().next());
    }

    public String getFileContentWithDefaultOption(String fileName, String defaultFileLocation, String ptsName, String dtsName, String dtsVersion, String subscriberName) throws IOException {
        try {
            return this.getFileContent(fileName, ptsName, dtsName, dtsVersion, subscriberName);
        } catch (IOException e) {
            return this.getFileContent(defaultFileLocation);
        }
    }
}
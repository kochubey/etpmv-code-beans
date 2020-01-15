package etpmv.canon.code.processors;

import com.jayway.jsonpath.JsonPath;
import com.jayway.jsonpath.ReadContext;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;

import static java.lang.String.format;

public class FileProcessor {

    private static final String DTS = "DSE/urn/pts/%s/dts/%s/%s";
    private static final String DTS_VERSION = "DSE/urn/pts/%s/dts/%s";
    private static final String SUBSCRIBERS = "DSE/urn/pts/%s/dts/%s/%s/subscribers/%s";
    private static final String PTS = "DSE/urn/pts/%s";
    private String webConfigFileLocation;

    public FileProcessor(String webConfigFileLocation) {
        this.webConfigFileLocation = webConfigFileLocation;
    }

    private String getFilePath(String ptsId, String dtsId, String dtsVersion, String subscriberId) {
        String filePath;

        if (ptsId != null) {
            if (dtsId != null) {
                if (dtsVersion != null) {
                    if (subscriberId != null) {
                        filePath = format(SUBSCRIBERS, ptsId, dtsId, dtsVersion, subscriberId);
                    } else {
                        filePath = format(DTS, ptsId, dtsId, dtsVersion);
                    }
                } else {
                    filePath = format(DTS_VERSION, ptsId, dtsId);
                }
            } else {
                filePath = format(PTS, ptsId);
            }
        } else {
            return null;
        }

        return filePath;
    }

    public String getFileContent(String fileName, String ptsId, String dtsId, String dtsVersion, String subscriberId) throws IOException {
        String filePath = getFilePath(ptsId, dtsId, dtsVersion, subscriberId);
        Path path = (filePath == null) ? Paths.get(webConfigFileLocation, fileName) : Paths.get(webConfigFileLocation, filePath, fileName);
        return String.join("", Files.readAllLines(path, StandardCharsets.UTF_8));
    }

    public String getFileContent(String fileId, String ptsId, String dtsId, String dtsVersion) throws IOException {
        return this.getFileContent(fileId, ptsId, dtsId, dtsVersion, null);
    }

    public String getFileContent(String fileName, String ptsId) throws IOException {
        return this.getFileContent(fileName, ptsId, null, null, null);
    }

    public String getFileContent(String fileName) throws IOException {
        return this.getFileContent(fileName, null, null, null, null);
    }

    public ArrayList<String> getList(String ptsId, String dtsId, String dtsVersion, String subscriberId) {
        Path path = Paths.get(webConfigFileLocation, getFilePath(ptsId, dtsId, dtsVersion, subscriberId));
        String[] pathArr = path.toFile().list();
        return (pathArr == null) ? new ArrayList<>(0) : new ArrayList<>(Arrays.asList(pathArr));
    }

    public ArrayList<String> getList(String ptsId, String dtsId, String dtsVersion) {
        return this.getList(ptsId, dtsId, dtsVersion, null);
    }

    public ArrayList<String> getList(String ptsId, String dtsId) {
        return this.getList(ptsId, dtsId, null);
    }

    public ArrayList<String> getList(String ptsId) {
        return this.getList(ptsId, null, null);
    }

    public static String getEndpointUrlFromJson(String fileContent) {
        ReadContext ctx = JsonPath.parse(fileContent);
        String targetUrl = ctx.read("$.servers[0].url");
        LinkedHashMap<String, String> targetPath = ctx.read("$.paths");
        return format("%s%s", targetUrl, targetPath.keySet().iterator().next());
    }

    public String getFileContentWithDefaultOption(String fileName, String defaultFileLocation, String ptsId, String dtsId, String dtsVersion, String subscriberId) throws IOException {
        try {
            return this.getFileContent(fileName, ptsId, dtsId, dtsVersion, subscriberId);
        } catch (IOException e) {
            return this.getFileContent(defaultFileLocation);
        }
    }
}
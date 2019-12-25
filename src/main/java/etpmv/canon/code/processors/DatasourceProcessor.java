package etpmv.canon.code.processors;

import java.io.IOException;
import java.nio.file.NoSuchFileException;
import java.util.ArrayList;
import java.util.List;

public class DatasourceProcessor {
    private FileProcessor fileProcessor;
    private String webConfigFileLocation;
    private final String DEFAULT_XSLT = "default.xslt";
    private final String CONFIG_YAML = "config.yaml";
    private final String DEFAULT_YAML = "default.yaml";
    private final String BODY_XSD = "body.xsd";
    private static final String FILTER_XML = "filter.xml";
    private final String ENDPOINT_JSON = "endpoint.json";

    public DatasourceProcessor(){}

    public DatasourceProcessor(String webConfigFileLocation) {
        this.webConfigFileLocation = webConfigFileLocation;
        this.fileProcessor = new FileProcessor(webConfigFileLocation);
    }

    public String datasource(String ptsId, String dtsId, String dtsVersion) throws IOException {
        return fileProcessor.getFileContent(BODY_XSD,  ptsId, dtsId, dtsVersion);
    }

    public List<String> subscribers(String ptsId, String dtsId, String dtsVersion) {
        return fileProcessor.getList(ptsId, dtsId, dtsVersion, "");
    }

    public String ptsEndpoint(String ptsId) throws IOException {
        return fileProcessor.getFileContent(ENDPOINT_JSON, ptsId);
    }

    public String dtsEndpoint(String ptsId, String dtsId, String dtsVersion) throws IOException {
        return fileProcessor.getFileContent(ENDPOINT_JSON, ptsId, dtsId, dtsVersion);
    }

    public String subscriberEndpoint(String ptsId, String dtsId, String dtsVersion, String subscriberId) throws IOException {
        return fileProcessor.getFileContent(ENDPOINT_JSON, ptsId, dtsId, dtsVersion, subscriberId);
    }

    public String ptsEndpointUrl(String ptsId) throws IOException {
        return fileProcessor.getEndpointUrlFromJson(ptsId);
    }

    public String dtsEndpointUrl(String ptsId, String dtsId, String dtsVersion) throws IOException {
        try {
            return fileProcessor.getEndpointUrlFromJson(ptsId, dtsId, dtsVersion);
        }catch (IOException e) {
            return fileProcessor.getEndpointUrlFromJson(ptsId);
        }
    }

    public String subscriberEndpointUrl(String ptsId, String dtsId, String dtsVersion, String subscriberId) throws IOException {
        try {
            return fileProcessor.getEndpointUrlFromJson(ptsId, dtsId, dtsVersion, subscriberId);
        }catch (IOException e) {
            return fileProcessor.getEndpointUrlFromJson(subscriberId);
        }
    }

    public String xsl(String ptsId, String dtsId, String dtsVersion, String subscriberId, String xslFileName) throws IOException {
        return fileProcessor.getFileContentWithDefaultOption(xslFileName, DEFAULT_XSLT, ptsId, dtsId, dtsVersion, subscriberId);
    }

    public String config(String ptsId, String dtsId, String dtsVersion, String subscriberId) throws IOException {
        return fileProcessor.getFileContentWithDefaultOption(CONFIG_YAML, DEFAULT_YAML, ptsId, dtsId, dtsVersion, subscriberId);
    }

    public List<String> ptsList() {
        return fileProcessor.getList("");
    }

    public List<String> dtsList(String ptsId) {
        if (ptsId==null) {
            return allDtsList();
        }
        return fileProcessor.getList(ptsId, "");
    }

    public List<String> allDtsList() {
        List<String> ptsList = ptsList();
        List<String> dtsList = new ArrayList<>();
        for (String ptsId : ptsList) {
            dtsList.addAll(dtsList(ptsId));
        }
        return dtsList;
    }

    public List<String> dtsVersions(String ptsId, String dtsId) {
        return fileProcessor.getList(ptsId, dtsId);
    }

    public String filterBody(String ptsId, String dtsId, String dtsVersion, String subscriber) {
        String filterBody = null;
        try {
            filterBody = fileProcessor.getFileContent(FILTER_XML, ptsId, dtsId,dtsVersion, subscriber);
        } catch (IOException ignored) {
        }

        return filterBody;
    }

    public String getWebConfigFileLocation() {
        return webConfigFileLocation;
    }

    public void setWebConfigFileLocation(String webConfigFileLocation) {
        this.webConfigFileLocation = webConfigFileLocation;
    }
}
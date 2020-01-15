package etpmv.canon.code.processors.datasource;

import etpmv.canon.code.exceptions.DataNotFoundException;
import etpmv.canon.code.processors.FileProcessor;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class FileDatasourceProcessor extends DatasourceProcessor {
    private FileProcessor fileProcessor;
    private final String ENDPOINT_JSON = "endpoint.json";

    public FileDatasourceProcessor(String webConfigFileLocation) {
        this.fileProcessor = new FileProcessor(webConfigFileLocation);
    }

    public String datasource(String ptsId, String dtsId, String dtsVersion) throws DataNotFoundException {
        try {
            String BODY_XSD = "body.xsd";
            return fileProcessor.getFileContent(BODY_XSD, ptsId, dtsId, dtsVersion);
        }catch (IOException e) {
            throw new DataNotFoundException(e.getMessage());
        }
    }

    public List<String> subscribers(String ptsId, String dtsId, String dtsVersion) {
        return fileProcessor.getList(ptsId, dtsId, dtsVersion, "");
    }

    public String ptsEndpoint(String ptsId) throws DataNotFoundException {
        try {
            return fileProcessor.getFileContent(ENDPOINT_JSON, ptsId);
        } catch (IOException e) {
            throw new DataNotFoundException(e.getMessage());
        }
    }

    public String dtsEndpoint(String ptsId, String dtsId, String dtsVersion) throws DataNotFoundException {
        try {
            return fileProcessor.getFileContent(ENDPOINT_JSON, ptsId, dtsId, dtsVersion);
        } catch (IOException e) {
            throw new DataNotFoundException(e.getMessage());
        }
    }

    public String subscriberEndpoint(String ptsId, String dtsId, String dtsVersion, String subscriberId) throws DataNotFoundException {
        try {
            return fileProcessor.getFileContent(ENDPOINT_JSON, ptsId, dtsId, dtsVersion, subscriberId);
        } catch (IOException e) {
            throw new DataNotFoundException(e.getMessage());
        }
    }

    public String xsl(String ptsId, String dtsId, String dtsVersion, String subscriberId, String xslFileName) throws DataNotFoundException {
        try {
            String DEFAULT_XSLT = "default.xslt";
            return fileProcessor.getFileContentWithDefaultOption(xslFileName, DEFAULT_XSLT, ptsId, dtsId, dtsVersion, subscriberId);
        } catch (IOException e) {
            throw new DataNotFoundException(e.getMessage());
        }
    }

    public String config(String ptsId, String dtsId, String dtsVersion, String subscriberId) throws DataNotFoundException {
        try {
            String CONFIG_YAML = "config.yaml";
            String DEFAULT_YAML = "default.yaml";
            return fileProcessor.getFileContentWithDefaultOption(CONFIG_YAML, DEFAULT_YAML, ptsId, dtsId, dtsVersion, subscriberId);
        } catch (IOException e) {
            throw new DataNotFoundException(e.getMessage());
        }
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

    private List<String> allDtsList() {
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
            String FILTER_XML = "filter.xml";
            filterBody = fileProcessor.getFileContent(FILTER_XML, ptsId, dtsId,dtsVersion, subscriber);
        } catch (IOException ignored) {
        }

        return filterBody;
    }
}
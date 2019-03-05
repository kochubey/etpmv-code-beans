package etpmv.canon.code.processors;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class DatasourceProcessor {
    FileProcessor fileProcessor = new FileProcessor();

    public DatasourceProcessor() {
        this.fileProcessor = fileProcessor;
    }

    public String getDatasource(String ptsId, String dtsId, String dtsVersion) throws IOException {
        return fileProcessor.getFileContent("body.xsd",  ptsId, dtsId, dtsVersion);
    }


    public List<String> getPtsList() {
        return fileProcessor.getList("");
    }

    public List<String> getDtsList(String ptsId) {
        return fileProcessor.getList(ptsId, "");
    }

    public List<String> getAllDtsList() {
        List<String> ptsList = getPtsList();
        List<String> dtsList = new ArrayList<>();
        for (String ptsId : ptsList) {
            dtsList.addAll(getDtsList(ptsId));
        }
        return dtsList;
    }

    public List<String> getDtsVersions(String ptsId, String dtsId) {
        return fileProcessor.getList(ptsId, dtsId);
    }

    public List<String> getSubscribers(String ptsId, String dtsId, String dtsVersion) {
        return fileProcessor.getList(ptsId, dtsId, dtsVersion, "");
    }
}
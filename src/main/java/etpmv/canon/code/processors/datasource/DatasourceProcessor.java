package etpmv.canon.code.processors.datasource;

import etpmv.canon.code.exceptions.DataNotFoundException;
import etpmv.canon.code.processors.FileProcessor;

import java.util.List;

public abstract class DatasourceProcessor {

    abstract String datasource(String ptsId, String dtsId, String dtsVersion) throws DataNotFoundException;

    public abstract List<String> subscribers(String ptsId, String dtsId, String dtsVersion) throws DataNotFoundException;

    abstract String ptsEndpoint(String ptsId) throws DataNotFoundException;

    abstract String dtsEndpoint(String ptsId, String dtsId, String dtsVersion) throws DataNotFoundException;

    abstract String subscriberEndpoint(String ptsId, String dtsId, String dtsVersion, String subscriberId) throws DataNotFoundException;

    public String ptsEndpointUrl(String ptsId) throws DataNotFoundException {
        return endpointUrl(this.ptsEndpoint(ptsId));
    }

    public String dtsEndpointUrl(String ptsId, String dtsId, String dtsVersion) throws DataNotFoundException {
        String endpointJson = null;
        try {
            endpointJson = this.dtsEndpoint(ptsId, dtsId, dtsVersion);
        }catch (DataNotFoundException ignored) {
        }

        if (endpointJson==null) endpointJson = this.ptsEndpoint(ptsId);

        return endpointUrl(endpointJson);
    }

    public String subscriberEndpointUrl(String ptsId, String dtsId, String dtsVersion, String subscriberId) throws DataNotFoundException {
        String endpointJson = null;
        try {
            endpointJson = this.subscriberEndpoint(ptsId, dtsId, dtsVersion, subscriberId);
        }catch (DataNotFoundException ignored) {
        }

        if (endpointJson==null) endpointJson = this.ptsEndpoint(subscriberId);

        return endpointUrl(endpointJson);
    }

    private String endpointUrl(String endpointJson) throws DataNotFoundException {
        if (endpointJson==null) throw new DataNotFoundException("Не найден файл endpoint.json");

        return FileProcessor.getEndpointUrlFromJson(endpointJson);
    }

    abstract String xsl(String ptsId, String dtsId, String dtsVersion, String subscriberId, String xslFileName) throws DataNotFoundException;

    abstract String config(String ptsId, String dtsId, String dtsVersion, String subscriberId) throws DataNotFoundException;

    public abstract List<String> ptsList();

    public abstract List<String> dtsList(String ptsId);

    public abstract List<String> dtsVersions(String ptsId, String dtsId);

    public abstract String filterBody(String ptsId, String dtsId, String dtsVersion, String subscriber) throws DataNotFoundException;
}

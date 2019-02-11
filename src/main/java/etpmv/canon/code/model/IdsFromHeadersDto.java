package etpmv.canon.code.model;

import org.apache.camel.Exchange;

@Deprecated
public class IdsFromHeadersDto {

    public static class Builder {
        private String ptsIdFromRequest;
        private String ptsIdFromResponse;
        private String ptsIdFromDatasource;
        private String datasourceId;

        public Builder(Exchange exchange) {
            String requestId = exchange.getIn().getHeader("X-Request-Id", String.class);
            String responseId = exchange.getIn().getHeader("X-Response-Id", String.class);
            String datasource = exchange.getIn().getHeader("X-Data-Source", String.class);

            ptsIdFromRequest = requestId.replace("urn:pts:", "");
            ptsIdFromRequest = ptsIdFromRequest.substring(0, ptsIdFromRequest.lastIndexOf(":"));

            if (responseId != null) {
                ptsIdFromResponse = responseId.replace("urn:pts:", "");
                ptsIdFromResponse = ptsIdFromResponse.substring(0, ptsIdFromResponse.lastIndexOf(":"));
            } else {
                ptsIdFromResponse = "";
            }

            datasourceId = datasource.replace("urn://dts/", "");
            ptsIdFromDatasource = datasourceId.substring(0, datasourceId.indexOf("/"));
            datasourceId = datasourceId.replace(String.format("%s%s", ptsIdFromDatasource, "/"), "");
        }

        public IdsFromHeadersDto build() {
            return new IdsFromHeadersDto(this);
        }
    }

    private String ptsIdFromRequest;
    private String ptsIdFromResponse;
    private String ptsIdFromDatasource;
    private String datasourceId;

    private IdsFromHeadersDto(Builder builder) {
        this.ptsIdFromRequest = builder.ptsIdFromRequest;
        this.ptsIdFromResponse = builder.ptsIdFromResponse;
        this.ptsIdFromDatasource = builder.ptsIdFromDatasource;
        this.datasourceId = builder.datasourceId;

    }

    public String getPtsIdFromRequest() {
        return ptsIdFromRequest;
    }

    public String getPtsIdFromResponse() {
        return ptsIdFromResponse;
    }

    public String getPtsIdFromDatasource() {
        return ptsIdFromDatasource;
    }

    public String getDatasourceId() {
        return datasourceId;
    }
}

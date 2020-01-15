package etpmv.canon.code.processors.datasource;

import javax.persistence.EntityManagerFactory;

public class DatasourceProcessorFactory {
    public static DatasourceProcessor createFileDatasourceProcessor(String webConfigFileLocation) {
        return new FileDatasourceProcessor(webConfigFileLocation);
    }

    public static DatasourceProcessor createDbDatasourceProcessor(EntityManagerFactory entityManagerFactory) {
        return new DbDatasourceProcessor(entityManagerFactory);
    }
}
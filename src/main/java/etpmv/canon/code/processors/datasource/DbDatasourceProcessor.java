package etpmv.canon.code.processors.datasource;

import etpmv.canon.code.entity.configs.Abonent;
import etpmv.canon.code.entity.configs.Config;
import etpmv.canon.code.entity.configs.DataType;
import etpmv.canon.code.entity.configs.Subscriber;
import etpmv.canon.code.exceptions.DataNotFoundException;
import org.apache.commons.io.IOUtils;

import javax.persistence.*;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class DbDatasourceProcessor extends DatasourceProcessor {
    private EntityManagerFactory entityManagerFactory;

    public DbDatasourceProcessor(EntityManagerFactory entityManagerFactory) {
        this.entityManagerFactory = entityManagerFactory;
    }

    private DataType dataType(String ptsId, String dtsId, String dtsVersion) throws DataNotFoundException {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        Query query = entityManager.createQuery("select dt " +
                "from DataType dt " +
                "inner join dt.abonent a " +
                "where a.code='" + ptsId + "' and dt.code='" + dtsId + "' and dt.version='" + dtsVersion + "'");
        DataType dataType = (DataType) query.getSingleResult();
        entityManager.close();
        if (dataType == null)
            throw new DataNotFoundException(String.format("Отсутсвует ВС urn://dts/%s/%s/%s", ptsId, dtsId, dtsVersion));
        return dataType;
    }

    private Subscriber subscriber(String ptsId, String dtsId, String dtsVersion, String subscriberId) throws DataNotFoundException {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        Query query = entityManager.createQuery("select sub " +
                "from Subscriber sub " +
                "inner join sub.dataType dt " +
                "inner join dt.abonent a " +
                "inner join sub.abonent sub_a " +
                "where sub_a.code='" + subscriberId + "' and dt.code='" + dtsId + "' and dt.version='" + dtsVersion + "' and a.code='" + ptsId + "'");

        Subscriber subscriber = (Subscriber) query.getSingleResult();
        entityManager.close();
        if (subscriber == null)
            throw new DataNotFoundException(String.format("Не найден подписант для ВС urn://dts/%s/%s/%s с id %s", ptsId, dtsId, dtsVersion, subscriberId));
        return subscriber;
    }

    public String datasource(String ptsId, String dtsId, String dtsVersion) throws DataNotFoundException {
        DataType dataType = dataType(ptsId, dtsId, dtsVersion);
        String body = dataType.getXsd();
        if (body == null)
            throw new DataNotFoundException(String.format("Отсутсвует ВС urn://dts/%s/%s/%s", ptsId, dtsId, dtsVersion));

        return body;
    }

    public List<String> subscribers(String ptsId, String dtsId, String dtsVersion) throws DataNotFoundException {
        DataType dataType = dataType(ptsId, dtsId, dtsVersion);
        List<Subscriber> subscribers = dataType.getSubscribers();
        return subscribers.stream().map(sub -> sub.getAbonent().getCode()).collect(Collectors.toList());
    }

    public String ptsEndpoint(String ptsId) throws DataNotFoundException {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        Query query = entityManager.createQuery("select a " +
                "from Abonent a " +
                "where a.code='" + ptsId + "'");
        Abonent abonent = (Abonent) query.getSingleResult();
        entityManager.close();
        if (abonent == null) throw new DataNotFoundException("Отсутствует ПТС с id " + ptsId);

        return abonent.getEndpoint();
    }

    public String dtsEndpoint(String ptsId, String dtsId, String dtsVersion) throws DataNotFoundException {
        DataType dataType = dataType(ptsId, dtsId, dtsVersion);

        return dataType.getEndpoint();
    }

    public String subscriberEndpoint(String ptsId, String dtsId, String dtsVersion, String subscriberId) throws DataNotFoundException {
        Subscriber subscriber = subscriber(ptsId, dtsId, dtsVersion, subscriberId);
        return subscriber.getEndpoint();
    }

    public String xsl(String ptsId, String dtsId, String dtsVersion, String subscriberId, String xslFileName) throws DataNotFoundException {
        Config config;
        if (subscriberId == null) {
            config = dataType(ptsId, dtsId, dtsVersion);
        } else {
            config = subscriber(ptsId, dtsId, dtsVersion, subscriberId);
        }

        String xslContent;
        if (xslFileName.startsWith("pack")) {
            xslContent = config.getPack();
        } else if (xslFileName.startsWith("unpack")) {
            xslContent = config.getUnpack();
        } else {
            throw new IllegalArgumentException("Unsupported xsl file name");
        }

        if (xslContent == null) {
            final String DEFAULT_XSLT = "default.xslt";
            return fileFromDefaultLocation(DEFAULT_XSLT);
        } else {
            return xslContent;
        }
    }

    public String config(String ptsId, String dtsId, String dtsVersion, String subscriberId) throws DataNotFoundException {
        Subscriber subscriber = subscriber(ptsId, dtsId, dtsVersion, subscriberId);
        String configContent = subscriber.getConfig();

        if (configContent == null) {
            String DEFAULT_YAML = "config.yaml";
            return fileFromDefaultLocation(DEFAULT_YAML);
        } else {
            return configContent;
        }
    }

    public List<String> ptsList() {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        TypedQuery<Abonent> query = entityManager.createQuery("select a from Abonent a", Abonent.class);
        List<Abonent> abonents = query.getResultList();
        entityManager.close();
        return abonents.stream()
                .map(Abonent::getCode)
                .collect(Collectors.toList());

    }

    public List<String> dtsList(String ptsId) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        TypedQuery<DataType> query;
        if (ptsId == null) {
            query = entityManager.createQuery("select dt from DataType dt", DataType.class);
        } else {
            query = entityManager.createQuery("select dt " +
                    "from DataType dt " +
                    "inner join dt.abonent a " +
                    "where a.code='" + ptsId + "'", DataType.class);
        }

        List<DataType> dataTypes = query.getResultList();
        entityManager.close();
        return dataTypes.stream()
                .map(DataType::getCode)
                .collect(Collectors.toList());
    }

    public List<String> dtsVersions(String ptsId, String dtsId) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        TypedQuery<DataType> query = entityManager.createQuery("select dt " +
                "from DataType dt " +
                "inner join dt.abonent a " +
                "where dt.code='" + dtsId + "' and a.code='" + ptsId + "'", DataType.class);

        List<DataType> dataTypes = query.getResultList();
        entityManager.close();
        return dataTypes.stream()
                .map(DataType::getVersion)
                .collect(Collectors.toList());
    }

    public String filterBody(String ptsId, String dtsId, String dtsVersion, String subscriber) throws DataNotFoundException {
        Config config;
        if (subscriber == null) {
            config = dataType(ptsId, dtsId, dtsVersion);
        } else {
            config = subscriber(ptsId, dtsId, dtsVersion, subscriber);
        }

        return config.getFilter();
    }

    private String fileFromDefaultLocation(String fileName) {
        final String DEFAULT_FILE_PATH = "settings/default";
        try (InputStream inputStream = Thread.currentThread().getContextClassLoader()
                .getResourceAsStream(String.format("%s/%s", DEFAULT_FILE_PATH, fileName))) {
            return IOUtils.toString(Objects.requireNonNull(inputStream), StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new IllegalArgumentException(String.format("File %s not found in path %s", fileName, DEFAULT_FILE_PATH));
        }
    }
}
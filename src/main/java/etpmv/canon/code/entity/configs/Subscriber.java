package etpmv.canon.code.entity.configs;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;

@Entity
@Table(name = "etp_sub")
public class Subscriber implements Config {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", updatable = false, nullable = false)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "vs_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private DataType dataType;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "abonent_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Abonent abonent;

    @Column(name = "endpoint")
    private String endpoint;

    @Column(name = "pack")
    private String pack;

    @Column(name = "unpack")
    private String unpack;

    @Column(name = "config")
    private String config;

    @Column(name = "filter")
    private String filter;

    public Abonent getAbonent() {
        return abonent;
    }

    public String getPack() {
        return pack;
    }

    public String getUnpack() {
        return unpack;
    }

    public String getEndpoint() {
        return endpoint;
    }

    public String getConfig() {
        return config;
    }

    public String getFilter() {
        return filter;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public DataType getDataType() {
        return dataType;
    }

    public void setDataType(DataType dataType) {
        this.dataType = dataType;
    }

    public void setAbonent(Abonent abonent) {
        this.abonent = abonent;
    }

    public void setEndpoint(String endpoint) {
        this.endpoint = endpoint;
    }

    public void setPack(String pack) {
        this.pack = pack;
    }

    public void setUnpack(String unpack) {
        this.unpack = unpack;
    }

    public void setConfig(String config) {
        this.config = config;
    }

    public void setFilter(String filter) {
        this.filter = filter;
    }
}

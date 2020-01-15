package etpmv.canon.code.entity.configs;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "etp_vs")
public class DataType implements Config {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", updatable = false, nullable = false)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "abonent_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Abonent abonent;

    @OneToMany(cascade = CascadeType.ALL,
            fetch = FetchType.EAGER,
            mappedBy = "dataType",
            orphanRemoval = true)
    private List<Subscriber> subscribers;

    @Column(name = "name")
    private String name;

    @Column(name = "code")
    private String code;

    @Column(name = "version")
    private String version;

    @Column(name = "xsd")
    private String xsd;

    @Column(name = "unpack")
    private String unpack;

    @Column(name = "pack")
    private String pack;

    @Column(name = "script")
    private String script;

    @Column(name = "endpoint")
    private String endpoint;

    @Column(name = "filter")
    private String filter;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Abonent getAbonent() {
        return abonent;
    }

    public void setAbonent(Abonent abonent) {
        this.abonent = abonent;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getXsd() {
        return xsd;
    }

    public void setXsd(String xsd) {
        this.xsd = xsd;
    }

    public String getUnpack() {
        return unpack;
    }

    public void setUnpack(String unpack) {
        this.unpack = unpack;
    }

    public String getPack() {
        return pack;
    }

    public void setPack(String pack) {
        this.pack = pack;
    }

    public String getScript() {
        return script;
    }

    public void setScript(String script) {
        this.script = script;
    }

    public String getEndpoint() {
        return endpoint;
    }

    public void setEndpoint(String endpoint) {
        this.endpoint = endpoint;
    }

    public String getFilter() {
        return filter;
    }

    public void setFilter(String filter) {
        this.filter = filter;
    }

    public List<Subscriber> getSubscribers() {
        return subscribers;
    }

    public void setSubscribers(List<Subscriber> subscribers) {
        this.subscribers = subscribers;
    }
}

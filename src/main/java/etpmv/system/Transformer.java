package etpmv.system;

public abstract class Transformer {
    private Data data;
    private String subscriber;

    public Transformer(Data data, String subscriber) {
        this.data = data;
        this.subscriber = subscriber;
    }

    public Data data() {
        return data;
    }

    public String subscriber() {
        return subscriber;
    }

    public abstract String path();

}


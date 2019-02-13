package etpmv.system;

public class Packer extends Transformer {
    public Packer(Data data, String subscriber) {
        super(data, subscriber);
    }

    @Override
    public String path() {
        if (data().isBroadcast()) return data().path() + "/pack.xsl";
        if (data().isAck()) return data().path() + "/" + data().subscriber() + "/pack.xsl";
        if (data().isRequest()) return data().path() + "/" + data().subscriber() + "/pack.xsl";
        if (data().isResponse()) return data().path() + "/pack.xsl";
        return null;
    }
}
